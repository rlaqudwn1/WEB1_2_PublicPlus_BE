package backend.dev.user.DTO.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "회원가입 요청 데이터")
public record UserJoinDTO(
        @Schema(description = "사용자 이메일", example = "example@example.com")
        @NotBlank
        String email,

        @Schema(description = "사용자 비밀번호", example = "password123")
        @NotBlank
        String password,

        @Schema(description = "비밀번호 확인", example = "password123")
        @NotBlank
        String checkPassword,

        @Schema(description = "사용자 닉네임", example = "만득이")
        @NotBlank
        String nickname) {

    @JsonIgnore
    public boolean isPasswordDifferent() {
        return !password.equals(checkPassword);
    }
}
