package backend.dev.review.service;

import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.repository.FacilityDetailsRepository;
import backend.dev.review.dto.ReviewDTO;
import backend.dev.review.entity.Review;
import backend.dev.review.repository.ReviewRepository;
import backend.dev.tag.entity.Tag;
import backend.dev.tag.enums.TagValue;
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

    public List<ReviewDTO> getReviewsByFacility(String facilityId) {
        List<Review> reviews = reviewRepository.findByFacility_FacilityIdOrderByCreatedAtDesc(facilityId);
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ReviewDTO createReview(String facilityId, ReviewDTO reviewDTO) {
        FacilityDetails facilityDetails = facilityDetailsRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("시설을 찾을 수 없습니다."));

        // 중복 리뷰 방지
        if (isDuplicateReview(facilityId, reviewDTO)) {
            throw new IllegalArgumentException("중복된 리뷰입니다.");
        }

        Review review = new Review();
        review.setFacility(facilityDetails);
        review.setReview_content(reviewDTO.getContent());
        review.setReview_rating(reviewDTO.getRating());
        review = reviewRepository.save(review);

        if (reviewDTO.getTags() != null) {
            Review finalReview = review;
            List<Tag> tags = reviewDTO.getTags().stream()
                    .map(tagValue -> new Tag(finalReview, TagValue.fromString(tagValue)))
                            .collect(Collectors.toList());
            tagRepository.saveAll(tags);
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
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        List<Tag> tags = tagRepository.findByReviewReviewId(reviewId);
        tagRepository.deleteAll(tags);

        reviewRepository.delete(review);
    }

    private boolean isDuplicateReview(String facilityId, ReviewDTO reviewDTO) {
        return reviewRepository.findByFacility_FacilityIdOrderByCreatedAtDesc(facilityId).stream()
                .anyMatch(review -> review.getReview_content().equals(reviewDTO.getContent()) &&
                        review.getReview_rating().equals(reviewDTO.getRating()));
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setFacilityId(review.getFacility().getFacilityId());
        dto.setContent(review.getReview_content());
        dto.setRating(review.getReview_rating());
        dto.setLikes(review.getReviewLikes());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());

        List<TagValue> tags = tagRepository.findByReviewReviewId(review.getReviewId())
                .stream()
                .map(Tag::getTagValue)
                .toList();
        dto.setTags(tags.stream().map(TagValue::getValue).collect(Collectors.toList()));

        return dto;
    }
}