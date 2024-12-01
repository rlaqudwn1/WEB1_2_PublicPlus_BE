package backend.dev.facility.exception;

import lombok.Data;
import lombok.Getter;

@Getter
public enum FacilityException {
    FACILITY_NOT_FOUND(404, "Facility not found"),
    FACILITY_EXCEPTION(401,"정해지지 않은 시설 오류입니다 발견하시면 카톡 부탁드려요"),
    DUPLICATE_FACILITY(409, "이미 있는 시설입니다"),
    INVALID_FACILITY_DATA(400, "시설 데이터 형식에 맞지 않습니다"),
    INVALID_CATEGORY(409, "카테고리형식이 맞지 않습니다"),
    FACILITY_ADD_FAILED(402,"시설 추가에 실패했습니다"),
    NOT_MODIFIED(401,"업데이트에 실패했습니다"),
    NOT_DELETED(401,"삭제에 실패했습니다"),
    INVALID_FILTER(402,"올바른 필터 설정이 아닙니다");
    private final int code;
    private final String message;

    FacilityException(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public FacilityTaskException getFacilityTaskException() {
        return new FacilityTaskException(message, code);
    }

}