package co.orange.ddanzi.dto.auth;

import co.orange.ddanzi.domain.user.Authentication;
import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.domain.user.enums.Nation;
import co.orange.ddanzi.domain.user.enums.Sex;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class VerifyRequestDto {
    private String ci;
    private String name;
    private String phone;
    private LocalDate birth;
    private Sex sex;
    private Boolean isAgreedMarketingTerm;

    public Authentication toEntity(User user, String phone){
        return Authentication.builder()
                .user(user)
                .ci(ci)
                .name(name)
                .phone(phone)
                .nation(Nation.KOR)
                .birth(birth)
                .sex(sex)
                .build();

    }
}
