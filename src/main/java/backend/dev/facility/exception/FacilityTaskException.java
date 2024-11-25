package backend.dev.facility.exception;

import lombok.Getter;

@Getter
public class FacilityTaskException extends RuntimeException {

  private final int code;
    public FacilityTaskException(String message, int code) {
        super(message);
        this.code = code;
    }
}
