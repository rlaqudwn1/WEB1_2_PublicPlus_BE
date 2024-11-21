package backend.dev.review.service;

import backend.dev.review.dto.ExReviewDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExReviewService {

    public List<ExReviewDTO> getExternalReviews(Long facilityId) {
        return List.of(
                new ExReviewDTO("시설이 매우 깨끗하고 좋아요!", "https://naver.blog/link1", 15, 100),
                new ExReviewDTO("농구 코트가 매우 넓고 좋아요!", "https://naver.blog/link2", 10, 80)
        );
    }
}