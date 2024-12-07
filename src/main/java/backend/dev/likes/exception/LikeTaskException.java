package backend.dev.likes.exception;


import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class LikeTaskException extends RuntimeException {
    private int code;
    public LikeTaskException(String message, int code) {
        super(message);
        this.code = code;
    }
}
