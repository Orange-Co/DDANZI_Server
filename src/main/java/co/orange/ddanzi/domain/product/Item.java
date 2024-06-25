package co.orange.ddanzi.domain.product;

import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.product.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Table(name = "items")
@Entity
public class Item {
    @Id
    @Column(name = "item_id")
    private UUID id;            //제품 고유 ID

    @Column(name = "img_url")
    private String imgUrl;      //등록시 업로드한 이미지

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;  //만료기간

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'EXPIRED'")
    @Column(name = "status")
    private ItemStatus status;  //상태(판매중/거래중/만료됨/거래완료)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;      //판매자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;    //상품


    @Builder
    public Item(String imgUrl, LocalDate dueDate, ItemStatus status, User seller, Product product) {
        this.imgUrl = imgUrl;
        this.dueDate = dueDate;
        this.status = status;
        this.seller = seller;
        this.product = product;
    }
}
