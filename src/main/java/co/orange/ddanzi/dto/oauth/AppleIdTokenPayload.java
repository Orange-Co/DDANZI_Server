package co.orange.ddanzi.dto.oauth;

import lombok.Getter;

@Getter
public class AppleIdTokenPayload {
    private String subject;
    private String email;
}
