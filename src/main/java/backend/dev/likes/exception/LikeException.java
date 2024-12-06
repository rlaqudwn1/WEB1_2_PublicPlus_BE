package backend.dev.likes.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LikeException {
    DUPLICATE_LIKE(400,"이미 좋아요를 눌렀습니다");

    private final int code;
    private final String message;



    public LikeTaskException getLikeException() {
        return new LikeTaskException(message, code);
    }
}
