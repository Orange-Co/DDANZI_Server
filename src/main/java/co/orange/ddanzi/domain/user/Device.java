package co.orange.ddanzi.domain.user;

import co.orange.ddanzi.domain.user.enums.DeviceType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Table(name = "device")
@Entity
public class Device {
    @Id
    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "type")
    private DeviceType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
