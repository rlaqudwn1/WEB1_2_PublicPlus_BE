package backend.dev.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExReviewDTO {
    private String title;
    private String sourceUrl;
    private LocalDateTime createdAt;
}