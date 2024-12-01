package backend.dev.user.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

public record ChangePasswordDTO(@Schema(description = "사용자 이메일", example = "example@example.com")
                                String email,

                                @Schema(description = "사용자 비밀번호", example = "password123")
                                String changePassword,

                                @Schema(description = "비밀번호 확인", example = "password123")
                                String checkChangePassword) {
    @JsonIgnore
    public boolean isSame(){
        return changePassword.equals(checkChangePassword);
    }
}
