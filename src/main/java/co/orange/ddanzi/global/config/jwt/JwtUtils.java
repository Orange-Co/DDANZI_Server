package co.orange.ddanzi.global.config.jwt;

import co.orange.ddanzi.global.common.error.Error;
import co.orange.ddanzi.global.common.exception.UnauthorizedException;
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

    public String createAccessToken(String idToken) {
        Claims claims = Jwts.claims();
        claims.put("idToken", idToken);

        long validTime = accessTokenTime;
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
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
        if (claims.get("idToken") == null) {
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
//        if(isLogout(token)){
//            throw new UnauthorizedException(Error.LOG_OUT_JWT_TOKEN);
//        }
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
            log.info("token \"id token\" : " + claims.get("idToken"));
            return true;
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(Error.INVALID_JWT_EXCEPTION);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(Error.JWT_EXPIRED);
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    public String getIdTokenFromToken(String token) {
        return getClaims(token).get("idToken").toString();
    }


    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
    }

    public boolean isLogout(String accessToken) {
        return !ObjectUtils.isEmpty(stringRedisTemplate.opsForValue().get(accessToken));
    }



}
