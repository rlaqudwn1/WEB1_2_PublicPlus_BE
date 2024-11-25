package backend.dev.setting.jwt;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

    private final Jwt jwt;

    public JwtToken makeToken(String userId) {
        Date now = new Date();
        String accessToken = jwt.sign("access_token", userId);
        String refreshToken = jwt.sign("refresh_token", userId);
        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .bearer("Bearer")
                .build();
    }

    public JwtToken resignAccessToken(String refreshToken) {

    }
}
