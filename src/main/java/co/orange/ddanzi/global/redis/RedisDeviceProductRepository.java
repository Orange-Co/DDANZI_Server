package co.orange.ddanzi.global.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class RedisDeviceProductRepository implements DeviceProductRepository{
    private final StringRedisTemplate redisTemplate;

    public RedisDeviceProductRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void addProductToDevice(String deviceToken, String productId) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.put("device:" + deviceToken, productId, "");
    }

    @Override
    public Set<String> getProductIdsForDevice(String deviceToken) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        return hashOps.keys("device:" + deviceToken);
    }

    @Override
    public boolean isProductAssociatedWithDevice(String deviceToken, String productId) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        return hashOps.hasKey("device:" + deviceToken, productId);
    }
}
