package backend.dev.setting.jwt;

import backend.dev.setting.redis.Redis;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

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
                .bearer("Bearer")
                .build();
    }

    public String resignAccessToken(String refreshToken, String userId) {
        String loginId = jwt.getLoginId(refreshToken);

        // refreshToKen 의 남은 유효 기간 가져오기
        Claims claims = jwt.parseClaims(refreshToken);
        long remainingTime = claims.getExpiration().getTime() - new Date().getTime();

        // refreshToKen 과 Id, 남은 시간을 BlackList 에 저장
        redis.setBlackList(refreshToken, loginId, remainingTime);

        return jwt.sign("access_token", userId);
    }

}
