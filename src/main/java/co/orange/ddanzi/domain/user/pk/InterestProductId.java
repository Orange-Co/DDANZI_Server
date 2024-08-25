package co.orange.ddanzi.domain.user.pk;

import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.product.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class InterestProductId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
