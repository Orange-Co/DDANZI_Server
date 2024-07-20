package co.orange.ddanzi.domain.user.enums;

import lombok.Getter;

@Getter
public enum DeviceType {
    ANDROID("ANDROID"),
    IOS("IOS")
    ;

    private final String deviceToken;
    DeviceType(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
