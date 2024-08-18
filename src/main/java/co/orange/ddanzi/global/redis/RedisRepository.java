package co.orange.ddanzi.global.redis;

import java.util.Set;

public interface RedisRepository {
    void saveDeviceToken(String deviceToken, String productId);

    Set<String> getRecentProducts(String deviceToken);

    void deleteDeviceToken(String deviceToken);
}