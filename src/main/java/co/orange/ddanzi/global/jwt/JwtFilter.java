package co.orange.ddanzi.global.jwt;

import co.orange.ddanzi.common.error.ErrorResponse;
import co.orange.ddanzi.common.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtils.resolveJWT(request);
        log.info("Request to {}: token={}", request.getRequestURI(), token);

        try {
            //home & search api
            if (isHomeOrSearchRequest(request.getRequestURI())) {
                handleHomeOrSearchRequest(token);
            }
            //other api
            else {
                handleGeneralRequest(token);
            }
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized Exception: {}", e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(ErrorResponse.onFailure(e.getError()).toJson());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/api/v1/auth/signin/test")
                || path.equals("/api/v1/auth/signin")
                || path.equals("/api/v1/health")
                || path.equals("/health-check")
                ;
    }

    private boolean isHomeOrSearchRequest(String requestURI) {
        return requestURI.startsWith("/api/v1/home") || requestURI.startsWith("/api/v1/search");
    }

    private void handleHomeOrSearchRequest(String token) {
        if (isValidLogoutToken(token)) {
            setAuthentication(token);
        } else {
            log.info("No valid token found for home or search request, proceeding without authentication");
            SecurityContextHolder.clearContext();
        }
    }

    private void handleGeneralRequest(String token) {
        if (isValidToken(token)) {
            setAuthentication(token);
        } else {
            log.info("No valid token found, proceeding without authentication");
        }
    }

    private boolean isValidToken(String token) {
        return token != null && !token.isEmpty() && jwtUtils.validateToken(token);
    }

    private boolean isValidLogoutToken(String token) {
        return token != null && !token.isEmpty() && jwtUtils.validateTokenInLogoutPage(token);
    }

    private void setAuthentication(String token) {
        Authentication authentication = jwtUtils.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
