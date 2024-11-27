package backend.dev.setting.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Redis {

    private final RedisTemplate<String, String> redisTemplate;

    public Redis(@Qualifier(value = "BlackList") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setBlackList(String token, String loginId, Long milliSeconds) {
        redisTemplate.opsForValue().set(token, loginId, milliSeconds, TimeUnit.MILLISECONDS);
    }

    public boolean hasTokenBlackList(String token) {
        return redisTemplate.hasKey(token);
    }
}
