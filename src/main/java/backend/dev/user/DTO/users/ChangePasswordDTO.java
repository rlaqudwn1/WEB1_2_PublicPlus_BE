package backend.dev.user.DTO.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDTO(@Schema(description = "사용자 이메일", example = "example@example.com")
                                @NotBlank
                                String email,

                                @Schema(description = "변경할 비밀번호", example = "password12345")
                                @NotBlank
                                String changePassword,

                                @Schema(description = "변경할 비밀번호 확인", example = "password12345")
                                @NotBlank
                                String checkChangePassword) {
    @JsonIgnore
    public boolean isDifferent() {
        return !changePassword.equals(checkChangePassword);
    }
}
