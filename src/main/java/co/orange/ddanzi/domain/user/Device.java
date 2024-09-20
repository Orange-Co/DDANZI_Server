package co.orange.ddanzi.domain.user;

import co.orange.ddanzi.domain.user.enums.DeviceType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Table(name = "devices")
@Entity
public class Device {
    @Id
    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "device_type")
    private String deviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Device(String deviceToken, String deviceType, User user) {
        this.deviceToken = deviceToken;
        this.deviceType = deviceType;
        this.user = user;
    }
}
