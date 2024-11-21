package backend.dev.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewDTO {
    private Long id;
    private Long facilityId;
    private String content;
    private Double rating;
    private List<String> tags;
    private Integer likes;
    private Integer views;
}
