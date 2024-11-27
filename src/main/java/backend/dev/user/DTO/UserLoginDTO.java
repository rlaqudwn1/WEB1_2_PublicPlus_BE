package backend.dev.user.DTO;

import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import org.springframework.util.StringUtils;

public record UserLoginDTO(
        String userEmail,
        String password
) {
public boolean checkPassword(){
    return StringUtils.hasText(password);
}
}
