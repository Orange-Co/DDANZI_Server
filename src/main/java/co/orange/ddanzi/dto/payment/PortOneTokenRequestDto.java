package co.orange.ddanzi.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PortOneTokenRequestDto {
    @JsonProperty("imp_key")
    private String impKey;  // 필드 이름을 camelCase로 수정

    @JsonProperty("imp_secret")
    private String impSecret;
}
