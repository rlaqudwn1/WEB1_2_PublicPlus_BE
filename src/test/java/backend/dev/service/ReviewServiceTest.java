package backend.dev.service;

import backend.dev.facility.entity.Facility;
import backend.dev.review.dto.ReviewDTO;
import backend.dev.review.entity.Review;
import backend.dev.review.repository.ReviewRepository;
import backend.dev.review.service.ReviewService;
import backend.dev.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private backend.dev.facility.repository.FacilityRepository facilityRepository;

    private Facility testFacility;
    private Review testReview;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testFacility = new Facility();
        testFacility.setFacilityId("test-facility-id");
        testFacility.changeFacilityName("잠실야구장");

        testReview = new Review();
        testReview.setReview_id(1L);
        testReview.setFacility(testFacility);
        testReview.setReview_content("Test Review");
        testReview.setReview_rating(4.5);
        testReview.setReviewLikes(10);
        testReview.setReviewViews(5);
    }

    @Test
    void testCreateReview() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setContent("훌륭한 시설입니다!");
        reviewDTO.setRating(5.0);

        when(facilityRepository.findById("test-facility-id")).thenReturn(Optional.of(testFacility));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> {
            Review savedReview = invocation.getArgument(0);
            savedReview.setReview_id(2L);
            return savedReview;
        });

        ReviewDTO createdReview = reviewService.createReview("test-facility-id", reviewDTO);

        assertThat(createdReview.getId()).isEqualTo(2L);
        assertThat(createdReview.getContent()).isEqualTo("훌륭한 시설입니다!");
        assertThat(createdReview.getRating()).isEqualTo(5.0);

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testGetReviewsByFacility() {
        when(reviewRepository.findByFacilityIdOrderByReviewLikesDesc("test-facility-id"))
                .thenReturn(Arrays.asList(testReview));

        List<ReviewDTO> reviews = reviewService.getReviewsByFacility("test-facility-id");

        assertThat(reviews).hasSize(1);
        assertThat(reviews.get(0).getContent()).isEqualTo("Test Review");
        assertThat(reviews.get(0).getRating()).isEqualTo(4.5);

        verify(reviewRepository, times(1)).findByFacilityIdOrderByReviewLikesDesc("test-facility-id");
    }

    @Test
    void testUpdateReview() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        ReviewDTO updatedDTO = new ReviewDTO();
        updatedDTO.setContent("Updated Test Review");
        updatedDTO.setRating(5.0);

        ReviewDTO updatedReview = reviewService.updateReview(1L, updatedDTO);

        assertThat(updatedReview.getContent()).isEqualTo("Updated Test Review");
        assertThat(updatedReview.getRating()).isEqualTo(5.0);

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testDeleteReview() {
        doNothing().when(reviewRepository).deleteById(1L);

        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1)).deleteById(1L);
    }
}