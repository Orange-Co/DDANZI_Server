package co.orange.ddanzi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TermInfo {
    private String term;
    private Boolean isAgreed;
}
