package co.orange.ddanzi.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PortOneTokenRequestDto {
    private String imp_key;
    private String imp_secret;
}
