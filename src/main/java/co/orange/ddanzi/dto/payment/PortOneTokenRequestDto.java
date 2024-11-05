package co.orange.ddanzi.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PortOneTokenRequestDto {
    @JsonProperty("imp_key")
    private String imp_key;
    @JsonProperty("imp_secret")
    private String imp_secret;
}
