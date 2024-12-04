package backend.dev.user.DTO.users;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.StringUtils;

public record UserLoginDTO(
        @Schema(description = "사용자 이메일", example = "example@example.com")
        String email,

        @Schema(description = "사용자 비밀번호", example = "password123")
        String password,

        String fcmToken
) {
public boolean checkPassword(){
    return StringUtils.hasText(password);
}
}
