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
    private String itemImgUrl;
    private LocalDate dueDate;
    private List<TermInfo> termItem;

    public Item toItem(User user, Product product){
        return Item.builder()
                .product(product)
                .seller(user)
                .imgUrl(itemImgUrl)
                .dueDate(dueDate)
                .status(ItemStatus.ON_SALE)
                .build();
    }
}
