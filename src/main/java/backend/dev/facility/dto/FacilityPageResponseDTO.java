package backend.dev.facility.dto;

import backend.dev.facility.dto.facility.FacilityResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class FacilityPageResponseDTO {
    @Schema(description = "현재 페이지의 시설 목록")
    private List<FacilityResponseDTO> content;

    @Schema(description = "총 페이지 수")
    private int totalPages;

    @Schema(description = "현재 페이지 번호")
    private int number;

    @Schema(description = "한 페이지에 포함된 시설 개수")
    private int size;

    @Schema(description = "전체 시설 개수")
    private long totalElements;
}
