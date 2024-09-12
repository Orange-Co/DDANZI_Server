package co.orange.ddanzi.service;

import co.orange.ddanzi.common.exception.DiscountNotFoundException;
import co.orange.ddanzi.common.exception.ItemNotFoundException;
import co.orange.ddanzi.common.exception.OrderNotFoundException;
import co.orange.ddanzi.common.exception.ProductNotFoundException;
import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.OrderOptionDetail;
import co.orange.ddanzi.domain.order.Payment;
import co.orange.ddanzi.domain.product.Discount;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.product.enums.ItemStatus;
import co.orange.ddanzi.domain.user.InterestProduct;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.common.AddressSeparateInfo;
import co.orange.ddanzi.dto.item.*;
import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.common.response.Success;
import co.orange.ddanzi.dto.mypage.MyItem;
import co.orange.ddanzi.global.jwt.AuthUtils;
import co.orange.ddanzi.repository.*;
import com.google.protobuf.Api;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Service
public class ItemService {
    private final AuthUtils authUtils;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderOptionDetailRepository orderOptionDetailRepository;
    private final InterestProductRepository interestProductRepository;

    @Autowired
    GcsService gcsService;
    @Autowired
    TermService termService;
    @Autowired
    AddressService addressService;
    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public ApiResponse<?> createSignedUrl(String fileName){
        String url = gcsService.generateSignedUrl(fileName);
        return ApiResponse.onSuccess(Success.GET_GCP_SIGNED_URL_SUCCESS, Map.of("signedUrl", url));
    }

    @Transactional
    public ApiResponse<?> saveItem(SaveItemRequestDto requestDto){
        if(requestDto.getReceivedDate().plusDays(7).isBefore(LocalDate.now()))
            return ApiResponse.onFailure(Error.DUE_DATE_IS_INCORRECT, null);

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
                .imgUrl(product.getImgUrl())
                .salePrice(product.getOriginPrice()-discount.getDiscountPrice())
                .build();
        return ApiResponse.onSuccess(Success.CREATE_ITEM_SUCCESS, responseDto);
    }

    @Transactional
    public ApiResponse<?> getItem(String itemId){
        User user = authUtils.getUser();
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        //check equals seller and user
        if(!item.getSeller().equals(user))
            return ApiResponse.onFailure(Error.ITEM_UNAUTHORIZED_USER, null);

        Order order = orderRepository.findByItem(item).orElse(null);
        Payment payment = null;
        if(order!=null)
            payment = paymentRepository.findByOrder(order);

        Product product = item.getProduct();
        Discount discount = discountRepository.findById(product.getId()).orElseThrow(DiscountNotFoundException::new);

        ItemResponseDto responseDto = ItemResponseDto.builder()
                .itemId(itemId)
                .imgUrl(product.getImgUrl())
                .status(order != null ? order.getStatus().toString() : item.getStatus().toString())
                .productName(product.getName())
                .originPrice(product.getOriginPrice())
                .salePrice(product.getOriginPrice()-discount.getDiscountPrice())
                .orderId(order != null ? order.getId() : null)
                .buyerNickName(order!=null ? order.getBuyer().getNickname() : null)
                .addressInfo(order!=null ? addressService.setAddressInfo(order.getBuyer()) : addressService.setAddressInfo(null))
                .paidAt(payment!=null ? payment.getEndedAt():null)
                .paymentMethod(payment!=null ? payment.getMethod().getDescription():null)
                .build();

        return ApiResponse.onSuccess(Success.GET_ITEM_PRODUCT_SUCCESS, responseDto);
    }


    @Transactional
    public ApiResponse<?> getAddressAndOption(String orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        AddressSeparateInfo address = addressService.setAddressSeparateInfo(order.getBuyer());

        List<SelectedOption> selectedOptionList = setSelectedOptionList(order);

        ItemAddressOptionResponseDto responseDto = ItemAddressOptionResponseDto.builder()
                .address(address.getAddress())
                .detailAddress(address.getDetailAddress())
                .zipCode(address.getZipCode())
                .recipient(address.getRecipient())
                .recipientPhone(address.getRecipientPhone())
                .selectedOptionList(selectedOptionList)
                .build();
        return ApiResponse.onSuccess(Success.GET_ORDER_ADDRESS_OPTION_SUCCESS, responseDto);
    }


    private String createItemId(Product product) {
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

    private List<SelectedOption> setSelectedOptionList(Order order){
        List<OrderOptionDetail> orderOptionDetailList = orderOptionDetailRepository.findAllByOrder(order);
        List<SelectedOption> selectedOptionList = new ArrayList<>();
        for(OrderOptionDetail orderOptionDetail : orderOptionDetailList){
            selectedOptionList.add(SelectedOption.builder()
                    .option(orderOptionDetail.getOptionDetail().getOption().getContent())
                    .selectedOption(orderOptionDetail.getOptionDetail().getContent())
                    .build());

        }
        return selectedOptionList;
    }

    public Integer getMyItemCount(User user){
        return itemRepository.countAllBySeller(user);
    }

    public List<MyItem> getMyItemList(User user){
        List<Item> itemList = itemRepository.findAllBySeller(user);
        List<MyItem> myItemList = new ArrayList<>();
        for(Item item : itemList){
            Product product = item.getProduct();
            Discount discount = discountRepository.findById(product.getId()).orElseThrow(DiscountNotFoundException::new);

            myItemList.add(MyItem.builder()
                    .productId(product.getId())
                    .itemId(item.getId())
                    .productName(product.getName())
                    .imgUrl(item.getImgUrl())
                    .originPrice(product.getOriginPrice())
                    .salePrice(product.getOriginPrice() - discount.getDiscountPrice())
                    .isInterested(interestProductRepository.existsByIdUserAndIdProduct(user,product))
                    .interestCount(interestProductRepository.countByProductIdWithLimit(product.getId()))
                    .build());
        }
        return myItemList;
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
