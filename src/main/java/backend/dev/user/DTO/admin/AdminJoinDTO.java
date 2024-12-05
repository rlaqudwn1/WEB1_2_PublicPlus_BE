package backend.dev.user.DTO.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "회원가입 요청 데이터")
public record AdminJoinDTO(
        @Schema(description = "관리자 이메일", example = "example@example.com")
        @NotBlank
        String email,

        @Schema(description = "관리자 비밀번호", example = "password123")
        @NotBlank
        String password,

        @Schema(description = "비밀번호 확인", example = "password123")
        @NotBlank
        String checkPassword,

        @Schema(description = "관리자 닉네임", example = "만득이")
        @NotBlank
        String nickname,

        @Schema(description = "관리자 가입 확인용 인증코드", example = "d3j-234hjfhj-234jg")
        @NotBlank
        String adminCode
) {
    @JsonIgnore
    public boolean isPasswordDifferent() {
        return !password.equals(checkPassword);
    }
}
