package backend.dev.user.DTO.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.util.StringUtils;

public record UserLoginDTO(
        @Schema(description = "사용자 이메일", example = "example@example.com")
        @NotEmpty
        String email,

        @Schema(description = "사용자 비밀번호", example = "password123")
        @NotEmpty
        String password,

        String fcmToken
) {
    public boolean isPasswordEmpty() {
        return !StringUtils.hasText(password);
    }
}
