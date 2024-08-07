package co.orange.ddanzi.global.config.jwt;

import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthUtils {
    private final UserRepository userRepository;

    public User getUser() {
        return userRepository.findByLoginId(getCurrentUserNickname()).get();
    }

    public Authentication getAuthentication() {
        // SecurityContext에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    public Object getPrincipal() {
        // 현재 사용자의 principal 가져오기
        return getAuthentication().getPrincipal();

    }

    public String getCurrentUserNickname() {
        Object principalObject = getPrincipal();

        if (principalObject instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principalObject;
            log.info("id token ->  {}", userDetails.getUsername());
            return userDetails.getUsername();
        }
        return null;
    }
}
