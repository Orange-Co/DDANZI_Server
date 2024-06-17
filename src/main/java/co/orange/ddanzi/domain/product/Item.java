package co.orange.ddanzi.domain.product;

import co.orange.ddanzi.domain.member.Member;
import co.orange.ddanzi.domain.product.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;            //제품 고유 ID

    @Column(name = "img_url")
    private String imgUrl;      //등록시 업로드한 이미지

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;  //만료기간

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ItemStatus status;  //상태(판매중/거래중/만료됨/거래완료)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "seller_id")
    private Member member;      //판매자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;    //상품
}
