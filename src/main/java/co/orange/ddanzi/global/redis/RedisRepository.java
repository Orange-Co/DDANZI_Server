package co.orange.ddanzi.global.redis;

import java.util.List;

public interface RedisRepository {
    void saveDeviceToken(String deviceToken, String productId);

    List<String> getRecentProducts(String deviceToken);

    void deleteDeviceToken(String deviceToken);
}