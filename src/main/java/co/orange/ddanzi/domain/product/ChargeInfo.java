package co.orange.ddanzi.domain.product;

import co.orange.ddanzi.domain.order.enums.PayMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "charge_infos")
@Entity
public class ChargeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charge_info_id")
    private Long id;

    @Column(name = "payment_method")
    private PayMethod paymentMethod;

    @Column(name = "charge")
    private Float charge;

}
