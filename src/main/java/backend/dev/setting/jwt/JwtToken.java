package backend.dev.setting.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtToken {
    private String authentication;
    private String accessToken;
    private String refreshToken;
    private String userId;
}
