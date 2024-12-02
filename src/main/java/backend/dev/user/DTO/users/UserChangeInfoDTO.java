package backend.dev.user.DTO.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserChangeInfoDTO(
        @Schema(description = "변경할 닉네임", example = "만득이")
        String nickname,

        @Schema(description = "변경할 소개글", example = "안녕하세요. 반갑습니다.")
        String description
) {
    private static final String NICKNAME_REGEX = "^[a-z0-9가-힣]{2,10}$";

    @JsonIgnore
    public boolean checkNickname() {
        return nickname.matches(NICKNAME_REGEX);
    }
}
