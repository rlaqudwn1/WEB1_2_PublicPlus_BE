package backend.dev.setting.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Component
public class Redis {

    private final RedisTemplate<String, String> redisTemplate;

    public Redis(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setBlackList(String token, String loginId, Duration milliSeconds) {
        redisTemplate.opsForValue().set(token, loginId, milliSeconds);
    }

    public boolean hasTokenBlackList(String token) {
        return Optional.ofNullable(redisTemplate.hasKey(token)).orElse(false);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        if (values.get(key) == null) {
            return "false";
        }
        return values.get(key);
    }

    public boolean checkExistsValue(String value) {
        return !value.equals("false");
    }
}

