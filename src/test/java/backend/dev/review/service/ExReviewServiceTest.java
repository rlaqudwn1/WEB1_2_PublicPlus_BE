package backend.dev.review.service;

import backend.dev.review.dto.ExReviewDTO;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExReviewServiceTest {

    private MockWebServer mockWebServer;
    private ExReviewService exReviewService;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        exReviewService = new ExReviewService(WebClient.builder(), baseUrl);
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

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponseBody)
                .addHeader("Content-Type", "application/json"));

        List<ExReviewDTO> reviews = exReviewService.getExternalReviews("Test Facility");

        assertNotNull(reviews);
        assertEquals(2, reviews.size());

        // 최신순으로 정렬되었는지 검증
        assertEquals("Newer Post", reviews.get(0).getTitle());
        assertEquals("https://newer-post.com", reviews.get(0).getSourceUrl());
        assertEquals("Older Post", reviews.get(1).getTitle());
        assertEquals("https://older-post.com", reviews.get(1).getSourceUrl());
    }

    @Test
    void testGetExternalReviewsErrorResponse() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500) // 500 에러 응답
                .addHeader("Content-Type", "application/json"));

        IllegalArgumentException exception = null;

        try {
            exReviewService.getExternalReviews("Test Facility");
        } catch (IllegalArgumentException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals("네이버 API 호출 실패 또는 데이터 없음", exception.getMessage());
    }
}