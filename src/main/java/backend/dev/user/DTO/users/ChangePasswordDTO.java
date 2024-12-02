package backend.dev.user.DTO.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

public record ChangePasswordDTO(@Schema(description = "사용자 이메일", example = "example@example.com")
                                String email,

                                @Schema(description = "변경할 비밀번호", example = "password12345")
                                String changePassword,

                                @Schema(description = "변경할 비밀번호 확인", example = "password12345")
                                String checkChangePassword) {
    @JsonIgnore
    public boolean isSame(){
        return changePassword.equals(checkChangePassword);
    }
}
