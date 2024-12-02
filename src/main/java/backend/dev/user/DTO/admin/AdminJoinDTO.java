package backend.dev.user.DTO.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 요청 데이터")
public record AdminJoinDTO(
        @Schema(description = "관리자 이메일", example = "example@example.com")
        String email,

        @Schema(description = "관리자 비밀번호", example = "password123")
        String password,

        @Schema(description = "비밀번호 확인", example = "password123")
        String checkPassword,

        @Schema(description = "관리자 닉네임", example = "만득이")
        String nickname,

        @Schema(description = "관리자 가입 확인용 인증코드", example = "d3j234hjfhj234jg")
        String adminCode
        ) {
    @JsonIgnore
    public boolean isSame() {
        return password.equals(checkPassword);
    }
}
