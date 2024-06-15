package co.orange.ddanzi.domain.member.pk;

import co.orange.ddanzi.domain.member.Member;
import co.orange.ddanzi.domain.product.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class InterestProductId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
