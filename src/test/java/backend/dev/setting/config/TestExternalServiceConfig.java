package backend.dev.setting.config;

import backend.dev.googlecalendar.setting.GoogleCalendarService;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@Configuration
@Profile("test")
public class TestExternalServiceConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        Map<String, String> store = new ConcurrentHashMap<>();

        @SuppressWarnings("unchecked")
        RedisTemplate<String, String> redisTemplate = Mockito.mock(RedisTemplate.class);
        @SuppressWarnings("unchecked")
        ValueOperations<String, String> valueOperations = Mockito.mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.hasKey(anyString())).thenAnswer(invocation -> store.containsKey(invocation.getArgument(0)));
        when(redisTemplate.delete(anyString())).thenAnswer(invocation -> store.remove(invocation.getArgument(0)) != null);
        when(valueOperations.get(anyString())).thenAnswer(invocation -> store.get(invocation.getArgument(0)));

        doAnswer(invocation -> {
            store.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(valueOperations).set(anyString(), anyString());
        doAnswer(invocation -> {
            store.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(valueOperations).set(anyString(), anyString(), Mockito.any(Duration.class));
        doAnswer(invocation -> {
            store.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(valueOperations).set(anyString(), anyString(), Mockito.anyLong(), Mockito.any(TimeUnit.class));

        return redisTemplate;
    }

    @Bean
    public GoogleCalendarService googleCalendarService() {
        return new GoogleCalendarService() {
            @Override
            public com.google.api.services.calendar.Calendar getCalendarService() throws IOException {
                throw new UnsupportedOperationException("Google Calendar is disabled in the test profile.");
            }
        };
    }
}
