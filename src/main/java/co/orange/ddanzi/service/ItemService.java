package co.orange.ddanzi.service;

import co.orange.ddanzi.common.exception.DiscountNotFoundException;
import co.orange.ddanzi.common.exception.ItemNotFoundException;
import co.orange.ddanzi.common.exception.ProductNotFoundException;
import co.orange.ddanzi.domain.product.Discount;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.product.enums.ItemStatus;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.item.SaveItemRequestDto;
import co.orange.ddanzi.dto.item.SaveItemResponseDto;
import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ItemService {
    private final AuthUtils authUtils;
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final DiscountRepository discountRepository;
    private final TermService termService;

    @Transactional
    public ApiResponse<?> saveItem(SaveItemRequestDto requestDto){
        User user = authUtils.getUser();
        if(user.getAuthentication() == null)
            return ApiResponse.onFailure(Error.AUTHENTICATION_INFO_NOT_FOUND, null);

        Product product = productRepository.findById(requestDto.getProductId()).orElseThrow(ProductNotFoundException::new);
        Discount discount = discountRepository.findById(product.getId()).orElseThrow(DiscountNotFoundException::new);

        String itemId = createItemId(product);
        Item newItem = requestDto.toItem(itemId, user, product);
        newItem = itemRepository.save(newItem);
        log.info("item 등록 성공, item_id: {}",newItem.getId());

        termService.createItemAgreements(newItem);

        product.updateStock(product.getStock() + 1);
        log.info("상품의 재고 수량 업데이트 -> {}개",  product.getStock());

        SaveItemResponseDto responseDto = SaveItemResponseDto.builder()
                .itemId(newItem.getId())
                .productName(product.getName())
                .salePrice(product.getOriginPrice()-discount.getDiscountPrice())
                .build();
        return ApiResponse.onSuccess(Success.CREATE_ITEM_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> getItem(String itemId){
        User user = authUtils.getUser();
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        return ApiResponse.onSuccess(Success.GET_ITEM_PRODUCT_SUCCESS, null);
    }

    public String createItemId(Product product) {
        String productId = product.getId();

        log.info("업로드 일자 포멧팅");
        LocalDate uploadDate= LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = uploadDate.format(formatter);

        log.info("{}의 max sequenceNumber 찾기",product.getName());
        Integer maxSequenceNumber = itemRepository.findMaxSequenceNumberByProduct(product);
        int nextSequenceNumber = (maxSequenceNumber != null ? maxSequenceNumber + 1 : 1);
        String sequenceNumberStr = String.format("%02d", nextSequenceNumber);
        log.info("{}의 next sequenceNumber -> {}",product.getName(),nextSequenceNumber);

        String itemId = productId + formattedDate + sequenceNumberStr;
        log.info("item_id 생성 완료 -> {}",itemId);

        return itemId;
    }

    public void deleteItemOfUser(User user) {

    }

    public void updateExpiredItems(){
        List<Item> itemList = itemRepository.findExpiryItems(LocalDate.now());
        for(Item item : itemList){
            item.updateStatus(ItemStatus.EXPIRED);
            Product product = item.getProduct();
            product.updateStock(product.getStock() - 1);
        }
    }
}
