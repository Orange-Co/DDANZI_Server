package co.orange.ddanzi.dto.payment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PortOneTokenRequestDto {
    private String imp_key;
    private String imp_secret;
}
