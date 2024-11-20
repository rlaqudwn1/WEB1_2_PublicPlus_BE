package backend.dev.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class PagingVO {
    private int page;             // 현재 페이지 번호
    private int recordSize;       // 페이지당 출력할 데이터 개수
    private int pageSize;         // 화면 하단에 출력할 페이지 사이즈
    private String keyword;       // 검색 키워드
    private String searchType;    // 검색 유형

    public PagingVO() {
        this.page = 1;           // 기본값: 첫 번째 페이지
        this.recordSize = 5;     // 기본값: 한 페이지당 5개 데이터
        this.pageSize = 5;       // 기본값: 하단 페이지 네비게이션 크기
    }

    // 현재 페이지에 대한 오프셋 계산
    public int getOffset() {
        return (page - 1) * recordSize;
    }

    // Pageable 객체 반환
    public Pageable getPageable() {
        return PageRequest.of(page - 1, recordSize); // PageRequest는 0-based index
    }
}
