package co.orange.ddanzi.global.redis;

import java.util.Set;

public interface DeviceProductRepository {
    void addProductToDevice(String deviceToken, String productId);
    Set<String> getProductIdsForDevice(String deviceToken);
    boolean isProductAssociatedWithDevice(String deviceToken, String productId);
}
