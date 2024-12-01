package backend.dev.review.naver;

import lombok.Data;

import java.util.List;

@Data
public class NaverApiResponse {
    private List<Item> items; // 네이버 API의 블로그 검색 결과
}

