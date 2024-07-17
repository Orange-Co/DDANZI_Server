package co.orange.ddanzi.global.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Slf4j
@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private static final String DEVICE_PREFIX = "device:";

    private final RedisTemplate<String, String> redisTemplate;

    public RedisRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void saveDeviceToken(String deviceToken, String productId) {
        redisTemplate.opsForList().leftPush(DEVICE_PREFIX + deviceToken, productId);
    }

    @Override
    public List<String> getRecentProducts(String deviceToken) {
        RedisOperations<String, String> operations = redisTemplate.opsForList().getOperations();
        return operations.opsForList().range(DEVICE_PREFIX + deviceToken, 0, -1);
    }

    @Override
    public void deleteDeviceToken(String deviceToken) {
        redisTemplate.delete(DEVICE_PREFIX + deviceToken);
    }
}
