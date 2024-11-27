package backend.dev.review.repository;

import backend.dev.facility.entity.FacilityDetails;
import backend.dev.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByFacilityDetails_FacilityIdOrderByReviewViewsDesc(String facilityDetailsId);
    List<Review> findByFacilityDetails_FacilityIdOrderByReviewLikesDesc(String facilityDetailsId);
}