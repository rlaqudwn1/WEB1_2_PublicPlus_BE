package backend.dev.review.service;

import backend.dev.review.dto.ExReviewDTO;
import backend.dev.review.naver.NaverApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExReviewService {

    private final WebClient webClient;

    @Value("${naver.api.clientId}")
    private String clientId;

    @Value("${naver.api.clientSecret}")
    private String clientSecret;

    public ExReviewService(WebClient.Builder webClientBuilder,
                           @Value("${naver.api.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public List<ExReviewDTO> getExternalReviews(String facilityId) {
        return fetchExternalReviews(facilityId);
    }

    private List<ExReviewDTO> fetchExternalReviews(String facilityId) {
        String query = facilityId + " 체육시설";

        Mono<NaverApiResponse> responseMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/search/blog.json")
                        .queryParam("query", query)
                        .queryParam("display", 10)
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        clientResponse -> Mono.error(new IllegalArgumentException("네이버 API 호출 실패 또는 데이터 없음"))
                )
                .bodyToMono(NaverApiResponse.class);

        NaverApiResponse response = responseMono.block();
        if (response == null || response.getItems() == null) {
            throw new IllegalArgumentException("네이버 API 호출 실패 또는 데이터 없음");
        }

        return response.getItems().stream()
                .map(item -> new ExReviewDTO(
                        item.getTitle(),
                        item.getLink(),
                        parseDate(item.getPostdate())
                ))
                .sorted(Comparator.comparing(ExReviewDTO::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    private LocalDateTime parseDate(String postDate) {
        try {
            LocalDate date = LocalDate.parse(postDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
            return date.atStartOfDay();
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}