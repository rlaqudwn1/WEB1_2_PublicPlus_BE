package backend.dev.review.dto;

import backend.dev.tag.enums.TagValue;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewDTO {
    private Long reviewId;
    private String facilityId;
    private String content;
    private Double rating;
    private List<TagValue> tags;
    private Integer likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
