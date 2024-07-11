package co.orange.ddanzi.domain;

import co.orange.ddanzi.global.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "banners")
@Entity
public class Banner extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Integer id;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "note")
    private String note;

    @Column(name = "is_selected", nullable = false)
    private Boolean isSelected;
}
