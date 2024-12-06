package backend.dev.likes.exception;

public class LikeTaskException extends RuntimeException {
    private int code;
    public LikeTaskException(String message, int code) {
        super(message);
        this.code = code;
    }
}
