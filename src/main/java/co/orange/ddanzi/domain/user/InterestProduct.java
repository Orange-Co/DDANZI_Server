package co.orange.ddanzi.domain.user;

import co.orange.ddanzi.domain.product.Product;
import co.orange.ddanzi.domain.user.pk.InterestProductId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "interest_products")
@Entity
public class InterestProduct {
    @EmbeddedId
    @Column(name = "interest_product_id")
    private InterestProductId id;

    @Builder
    public InterestProduct(final InterestProductId id) {
        this.id=id;
    }

    public InterestProduct toEntity(User user, Product product) {
        InterestProductId interestProductId = new InterestProductId();
        interestProductId.setUser(user);
        interestProductId.setProduct(product);

        return InterestProduct.builder()
                .id(interestProductId)
                .build();
    }
}
