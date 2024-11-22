package backend.dev.review.controller;

import backend.dev.review.dto.ExReviewDTO;
import backend.dev.review.dto.ReviewDTO;
import backend.dev.review.service.ExReviewService;
import backend.dev.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facility-details/{id}/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ExReviewService exReviewService;

    public ReviewController(ReviewService reviewService, ExReviewService exReviewService) {
        this.reviewService = reviewService;
        this.exReviewService = exReviewService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getReviewsByFacility(@PathVariable String facilityId) {

        List<ReviewDTO> internalReviews = reviewService.getReviewsByFacility(facilityId);

        List<ExReviewDTO> externalReviews = exReviewService.getExternalReviews(facilityId);

        return ResponseEntity.ok(Map.of(
                "internalReviews", internalReviews,
                "externalReviews", externalReviews
        ));
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@PathVariable String facilityId, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = reviewService.createReview(facilityId, reviewDTO);
        return ResponseEntity.ok(createdReview);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(reviewId, reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(Map.of("알림", "리뷰 삭제 완료"));
    }
}