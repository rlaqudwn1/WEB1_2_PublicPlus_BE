package backend.dev.setting.exception;

import lombok.Getter;

@Getter
public class PublicPlusCustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public PublicPlusCustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
