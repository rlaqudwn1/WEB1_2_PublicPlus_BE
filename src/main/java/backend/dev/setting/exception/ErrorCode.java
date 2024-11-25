package backend.dev.setting.exception;

import static org.springframework.http.HttpStatus.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_FOUND_USER(NOT_FOUND, "가입된 회원을 찾을 수 없습니다"),
    NOT_FOUND_EMAIL(NOT_FOUND,"가입된 이메일이 아닙니다"),
    DUPLICATE_EMAIL(BAD_REQUEST,"이미 가입된 이메일입니다"),
    NOT_MATCH_PASSWORD(BAD_REQUEST,"암호가 일치하지 않습니다"),
    BAD_NICKNAME(BAD_REQUEST,"닉네임을 다시 설정해주세요"),
    PROFILE_CREATE_DIRECTORY_FAIL(BAD_GATEWAY,"디렉토리 설정이 실패했습니다"),
    WRONG_FILE_TYPE(BAD_REQUEST,"파일타입이 맞지 않습니다"),
    PROFILE_DELETE_FAIL(BAD_REQUEST,"프로필 삭제가 실패했습니다. 다시 시도해주세요"),
    PROFILE_INVALID_FILE(BAD_REQUEST,"파일이 존재하지 않습니다"),
    PROFILE_INVALID_FILE_TYPE(BAD_REQUEST,"파일 타입이 맞지 않습니다"),
    NOT_MATCH_EMAIL_OR_PASSWORD(BAD_REQUEST, "이메일이나 암호가 맞지 않습니다"),
    INVALID_TOKEN(BAD_REQUEST,"유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(BAD_REQUEST,"만료된 토큰입니다"),
    UNSUPPORTED_TOKEN(BAD_REQUEST,"지원하지 않는 토큰입니다"), NOT_FOUND_TOKEN(BAD_REQUEST,"토큰을 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
