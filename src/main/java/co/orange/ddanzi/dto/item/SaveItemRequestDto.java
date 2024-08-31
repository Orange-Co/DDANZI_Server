package co.orange.ddanzi.dto.item;

import co.orange.ddanzi.domain.product.Item;
import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.product.enums.ItemStatus;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.TermInfo;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class SaveItemRequestDto {
    private String productId;
    private String productName;
    private LocalDate dueDate;
    private String registeredImage;

    public Item toItem(String id, User user, Product product){
        return Item.builder()
                .id(id)
                .product(product)
                .seller(user)
                .imgUrl(registeredImage)
                .dueDate(dueDate)
                .status(ItemStatus.ON_SALE)
                .build();
    }
}
