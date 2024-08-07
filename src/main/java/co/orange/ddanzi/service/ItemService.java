package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.item.SaveItemRequestDto;
import co.orange.ddanzi.dto.item.SaveItemResponseDto;
import co.orange.ddanzi.global.common.error.Error;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Slf4j
@RequiredArgsConstructor
@Service
public class ItemService {
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public ApiResponse<?> saveItem(User user, SaveItemRequestDto requestDto){
        Product product = productRepository.findById(requestDto.getProductId()).orElse(null);
        if(product == null)
            return ApiResponse.onFailure(Error.PRODUCT_NOT_FOUND, null);

        String itemId = createItemId(product);
        Item newItem = requestDto.toItem(itemId, user, product);
        newItem = itemRepository.save(newItem);
        log.info("item 등록 성공, item_id: {}",newItem.getId());

        product.updateStock(product.getStock() + 1);
        log.info("상품의 재고 수량 업데이트 -> {}개",  product.getStock());

        SaveItemResponseDto responseDto = SaveItemResponseDto.builder()
                .itemId(newItem.getId())
                .productName(product.getName())
                .originPrice(product.getOriginPrice())
                .build();
        return ApiResponse.onSuccess(Success.CREATE_ITEM_SUCCESS, responseDto);
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
}
