package co.orange.ddanzi.global.config.redis;

import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.global.common.response.Success;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RedisController {

    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping("/redisTest")
    public ApiResponse<?> addRedisKey() {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        vop.set("red", "apple");
        vop.set("yellow", "apple");
        vop.set("green", "watermelon");

        return ApiResponse.onSuccess(Success.SET_REDIS_KEY_SUCCESS, null);
    }

    @GetMapping("/redisTest/{key}")
    public ApiResponse<?> getRedisKey(@PathVariable String key) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String value = vop.get(key);
        return ApiResponse.onSuccess(Success.GET_REDIS_KEY_SUCCESS, value);
    }
}
