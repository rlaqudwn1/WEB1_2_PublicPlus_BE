package backend.dev.setting.jwt;

import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.setting.redis.Redis;
import io.jsonwebtoken.Claims;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

    private final Jwt jwt;
    private final Redis redis;

    public JwtToken makeToken(String userId) {
        String accessToken = jwt.sign("access_token", userId);
        String refreshToken = jwt.sign("refresh_token", userId);
        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userId)
                .authentication("Bearer")
                .build();
    }


    public JwtToken resignAccessToken(String refreshToken) {
        if(!jwt.isRefreshToken(refreshToken)) throw new PublicPlusCustomException(ErrorCode.INVALID_TOKEN);

        // refresh_token 의 남은 시간 추출
        Claims claims = jwt.parseClaims(refreshToken);
        String userId = claims.getId();
        Duration remainingTime = Duration.of(claims.getExpiration().getTime() - new Date().getTime(),
                TimeUnit.MILLISECONDS.toChronoUnit());

        // refreshToKen 과 Id, 남은 시간을 redis 에 저장
        redis.setValues(refreshToken, userId, remainingTime);

        String resignedAccessToken =jwt.sign("access_token", userId);
        return JwtToken.builder().accessToken(resignedAccessToken).refreshToken(refreshToken).userId(userId).authentication("Bearer").build();
    }

    public void removeToken(String refreshToken) {
        if(!jwt.isRefreshToken(refreshToken)&&!redis.removeValues(refreshToken)) throw new PublicPlusCustomException(ErrorCode.INVALID_TOKEN);
    }

}
