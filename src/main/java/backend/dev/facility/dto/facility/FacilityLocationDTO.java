package backend.dev.facility.dto.facility;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FacilityLocationDTO {
    @Schema(description = "검색할 시설의 위도", example = "37.436429919747084")
    private Double latitude;

    @Schema(description = "검색할 시설의 경도", example = "127.01409711887952")
    private Double longitude;

    @Schema(description = "검색할 반경 (킬로미터 단위)", example = "5")
    private Double radius;
}