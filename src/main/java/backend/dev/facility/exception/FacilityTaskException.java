package backend.dev.facility.exception;

public class FacilityTaskException extends RuntimeException {

  private final int code;
    public FacilityTaskException(String message, int code) {
        super(message);
        this.code = code;
    }
}
