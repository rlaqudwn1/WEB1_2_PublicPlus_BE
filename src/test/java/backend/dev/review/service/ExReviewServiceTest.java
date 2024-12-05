package backend.dev.review.service;

import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.repository.FacilityDetailsRepository;
import backend.dev.review.dto.ExReviewDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ExReviewServiceTest {

    private MockWebServer mockWebServer;
    private ExReviewService exReviewService;

    @Mock
    private FacilityDetailsRepository facilityDetailsRepository;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        facilityDetailsRepository = mock(FacilityDetailsRepository.class);

        String baseUrl = mockWebServer.url("/").toString();
        exReviewService = new ExReviewService(WebClient.builder(), baseUrl, facilityDetailsRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void testGetExternalReviewsDefault() throws Exception {
        String mockResponseBody = """
        {
            "items": [
                {
                    "title": "Older Post",
                    "link": "https://older-post.com",
                    "description": "Description 1",
                    "bloggername": "Blogger1",
                    "bloggerlink": "https://link1.com",
                    "postdate": "20241028"
                },
                {
                    "title": "Newer Post",
                    "link": "https://newer-post.com",
                    "description": "Description 2",
                    "bloggername": "Blogger2",
                    "bloggerlink": "https://link2.com",
                    "postdate": "20241129"
                }
            ]
        }
        """;

        when(facilityDetailsRepository.findById("test-facility-id"))
                .thenReturn(Optional.of(FacilityDetails.builder()
                        .facilityName("Test Facility")
                        .build()));

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponseBody)
                .addHeader("Content-Type", "application/json"));

        List<ExReviewDTO> reviews = exReviewService.getExternalReviews("test-facility-id");

        assertNotNull(reviews);
        assertEquals(2, reviews.size());

        assertEquals("Newer Post", reviews.get(0).getTitle());
        assertEquals("https://newer-post.com", reviews.get(0).getSourceUrl());
        assertEquals("Older Post", reviews.get(1).getTitle());
        assertEquals("https://older-post.com", reviews.get(1).getSourceUrl());

        verify(facilityDetailsRepository, times(1)).findById("test-facility-id");
    }

    @Test
    void testGetExternalReviewsErrorResponse() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .addHeader("Content-Type", "application/json"));

        IllegalArgumentException exception = null;

        try {
            exReviewService.getExternalReviews("test-facility-id");
        } catch (IllegalArgumentException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals("시설을 찾을 수 없습니다.", exception.getMessage());
    }
}