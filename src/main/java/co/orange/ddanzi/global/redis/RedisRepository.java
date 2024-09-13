package co.orange.ddanzi.global.redis;

import java.util.List;

public interface RedisRepository {
    void saveRecentProduct(String deviceToken, String productId);

    List<String> getRecentProducts(String deviceToken);

    void deleteRecentProduct(String deviceToken);
}