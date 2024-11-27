package backend.dev.review.service;

import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.repository.FacilityDetailsRepository;
import backend.dev.review.dto.ReviewDTO;
import backend.dev.review.entity.Review;
import backend.dev.review.repository.ReviewRepository;
import backend.dev.tag.entity.Tag;
import backend.dev.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final FacilityDetailsRepository facilityDetailsRepository;
    private final TagRepository tagRepository;

    public ReviewService(ReviewRepository reviewRepository, FacilityDetailsRepository facilityDetailsRepository, TagRepository tagRepository) {
        this.reviewRepository = reviewRepository;
        this.facilityDetailsRepository = facilityDetailsRepository;
        this.tagRepository = tagRepository;
    }

    public List<ReviewDTO> getReviewsByFacility(String id) {
        List<Review> reviews = reviewRepository.findByFacilityDetails_FacilityIdOrderByReviewLikesDesc(id);
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ReviewDTO createReview(String facilityId, ReviewDTO reviewDTO) {
        FacilityDetails facility = facilityDetailsRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("시설을 찾을 수 없습니다."));

        Review review = new Review();
        review.setFacilityDetails(facility);
        review.setReview_content(reviewDTO.getContent());
        review.setReview_rating(reviewDTO.getRating());
        review = reviewRepository.save(review);

        if (reviewDTO.getTags() != null) {
            Review finalReview = review;
            reviewDTO.getTags().forEach(tag -> {
                Tag newtag = new Tag();
                newtag.setReview(finalReview);
                newtag.setTag(tag);
                tagRepository.save(newtag);
            });
        }
        return convertToDTO(review);
    }

    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        review.setReview_content(reviewDTO.getContent());
        review.setReview_rating(reviewDTO.getRating());
        review = reviewRepository.save(review);

        return convertToDTO(review);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getReview_id());
        dto.setFacilityId(review.getFacilityDetails().getFacilityId());
        dto.setContent(review.getReview_content());
        dto.setRating(review.getReview_rating());
        dto.setLikes(review.getReviewLikes());
        dto.setViews(review.getReviewViews());
        return dto;
    }
}