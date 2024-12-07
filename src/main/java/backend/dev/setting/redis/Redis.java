package backend.dev.setting.redis;

import java.time.Duration;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class Redis {

    private final RedisTemplate<String, String> redisTemplate;

    public Redis(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    @Transactional(readOnly = true)
    public boolean isHasValues(String key) {
        return Optional.ofNullable(redisTemplate.hasKey(key)).orElse(false);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    @Transactional
    public boolean removeValues(String key) {
        return Optional.ofNullable(redisTemplate.delete(key)).orElse(false);
    }

}

