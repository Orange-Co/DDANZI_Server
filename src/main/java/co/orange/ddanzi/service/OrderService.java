package co.orange.ddanzi.service;

import co.orange.ddanzi.domain.product.Discount;
import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.user.Address;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.AddressInfo;
import co.orange.ddanzi.dto.order.CheckProductResponseDto;
import co.orange.ddanzi.global.common.exception.ItemNotFoundException;
import co.orange.ddanzi.global.common.exception.ProductNotFoundException;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import co.orange.ddanzi.global.config.jwt.AuthUtils;
import co.orange.ddanzi.repository.AddressRepository;
import co.orange.ddanzi.repository.DiscountRepository;
import co.orange.ddanzi.repository.ItemRepository;
import co.orange.ddanzi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final AuthUtils authUtils;
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final DiscountRepository discountRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public ApiResponse<?> checkOrderProduct(String productId){

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException());
        Item item = itemRepository.findNearestExpiryItem(product).orElseThrow(()-> new ItemNotFoundException());

        Discount discount = discountRepository.findById(productId).orElse(null);

        User user = authUtils.getUser();
        Address address = addressRepository.findByUser(user);

        AddressInfo addressInfo = AddressInfo.builder()
                .recipient(address.getRecipient())
                .zipCode(address.getZipCode())
                .address(address.getAddress()+" "+address.getDetailAddress())
                .recipientPhone(address.getRecipientPhone())
                .build();

        CheckProductResponseDto responseDto = CheckProductResponseDto.builder()
                .itemId(item.getId())
                .productName(product.getName())
                .imgUrl(product.getImgUrl())
                .originPrice(product.getOriginPrice())
                .addressInfo(addressInfo)
                .discountPrice(discount.getDiscountPrice())
                .charge(100)
                .totalPrice(product.getOriginPrice() - discount.getDiscountPrice())
                .build();
        return ApiResponse.onSuccess(Success.GET_ORDER_PRODUCT_SUCCESS, responseDto);
    }
}
