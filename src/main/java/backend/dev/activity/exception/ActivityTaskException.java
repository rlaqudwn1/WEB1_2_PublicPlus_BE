package backend.dev.activity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityTaskException extends RuntimeException {
    private int code;
    public ActivityTaskException(int code,String message) {
        super(message);
        this.code= code;
    }
}
