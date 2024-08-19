package co.orange.ddanzi.global.jwt;

import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.exception.UnauthorizedException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtils {

    @Value("${secret.time.access}")
    private long accessTokenTime;

    @Value("${secret.time.refresh}")
    private long refreshTokenTime;

    @Value("${secret.key}")
    private String jwtSecretKey;

    private final StringRedisTemplate stringRedisTemplate;

    public String createAccessToken(String email) {
        Claims claims = Jwts.claims();
        claims.put("email", email);

        long validTime = accessTokenTime;
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    public String createRefreshToken(String email) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        long validTime = refreshTokenTime;
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();

        updateUserRefreshToken(email, refreshToken);

        return refreshToken;
    }

    public void updateUserRefreshToken(String email, String refreshToken) {
        stringRedisTemplate.opsForValue().set(email, refreshToken, refreshTokenTime, TimeUnit.MILLISECONDS);
    }

    public String resolveJWT(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }

    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = getClaims(token);
        if (claims.get("email") == null) {
            log.error("UnauthorizedException will be thrown due to missing email claim");
            throw new UnauthorizedException(Error.INVALID_JWT_EXCEPTION);
        }

        // UserDetails 객체를 생성하여 Authentication 반환
        UserDetails principal = new User(getIdTokenFromToken(token), "", Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(principal, "", Collections.emptyList());
    }



    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new UnauthorizedException(Error.JWT_TOKEN_NOT_EXISTS);
        }
        if(isLogout(token)){
            throw new UnauthorizedException(Error.LOG_OUT_JWT_TOKEN);
        }
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
            log.info("token \"id token\" : " + claims.get("email"));
            return true;
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(Error.INVALID_JWT_EXCEPTION);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(Error.JWT_EXPIRED);
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    public boolean isValidRefreshToken(String email, String refreshToken) {
        if (email == null) {
            return false; // 이메일이 null인 경우 유효하지 않음
        }
        String storedRefreshToken = stringRedisTemplate.opsForValue().get(email);
        return refreshToken.equals(storedRefreshToken);
    }


    public boolean validateTokenInLogoutPage(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        if(isLogout(token)){
            return false;
        }
        try {
                log.info("home or search api");
                Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
                log.info("token \"id token\" : " + claims.get("email"));
                return true;
            }
        catch (ExpiredJwtException e) {
            log.info("Expired token. Processing with log out");
            return false;
        }
        catch (MalformedJwtException e) {
            return false;
        } catch (UnauthorizedException e) {
            return false;
        }
    }


    public String getIdTokenFromToken(String token) {
        return getClaims(token).get("email").toString();
    }

    public String getIdFromRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("email").toString(); // 이메일 정보 반환
        } catch (Exception e) {
            // 토큰이 유효하지 않거나 에러 발생 시 null 반환
            return null;
        }
    }


    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
    }

    public boolean isLogout(String accessToken) {
        return !ObjectUtils.isEmpty(stringRedisTemplate.opsForValue().get(accessToken));
    }



}
