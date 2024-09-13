package co.orange.ddanzi.global.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Slf4j
@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private static final String DEVICE_PREFIX = "device:";
    private static final long MAX_RECENT_PRODUCTS = 12;
    private static final long EXPIRATION_TIME = 10 * 24 * 60 * 60; // 10일

    private final RedisTemplate<String, String> redisTemplate;

    public RedisRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveRecentProduct(String deviceToken, String productId) {
        String key = DEVICE_PREFIX + deviceToken;
        redisTemplate.opsForList().leftPush(key, productId);
        redisTemplate.opsForList().trim(key, 0, MAX_RECENT_PRODUCTS - 1); // 리스트 길이 제한
        redisTemplate.expire(key, EXPIRATION_TIME, TimeUnit.SECONDS);

    }

    @Override
    public List<String> getRecentProducts(String deviceToken) {
        String key = DEVICE_PREFIX + deviceToken;
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    @Override
    public void deleteRecentProduct(String deviceToken) {
        redisTemplate.delete(DEVICE_PREFIX + deviceToken);
    }
}
