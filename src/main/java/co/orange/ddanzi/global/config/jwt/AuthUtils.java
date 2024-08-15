package co.orange.ddanzi.global.config.jwt;

import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.global.common.exception.UserNotFoundException;
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
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        String currentUserNickname = getCurrentUserNickname();
        if (currentUserNickname == null) {
            return null;
        }
        return userRepository.findByEmail(currentUserNickname)
                .orElseThrow(() -> new UserNotFoundException());

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
            log.info("email ->  {}", userDetails.getUsername());
            return userDetails.getUsername();
        }
        return null;
    }
}
