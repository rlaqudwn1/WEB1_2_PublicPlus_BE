package backend.dev.setting.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtToken {
    private String bearer;
    private String accessToken;
    private String refreshToken;
}
