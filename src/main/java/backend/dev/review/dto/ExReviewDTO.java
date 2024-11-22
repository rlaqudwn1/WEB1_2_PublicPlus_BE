package backend.dev.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExReviewDTO {
    private String content;
    private String sourceUrl;
    private Integer likes;
    private Integer views;

    public ExReviewDTO(String con, String url, int like, int view) {
    }
}