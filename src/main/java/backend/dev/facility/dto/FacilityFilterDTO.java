package backend.dev.facility.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityFilterDTO {

    @Schema(description = "시설 이름", example = "", defaultValue = "")
    private String facilityName; // 시설 이름, 기본값은 빈 문자열

    @Schema(description = "시설 카테고리", example = "축구장", defaultValue = "축구장")
    private String facilityCategory; // 시설 카테고리, 기본값은 "축구장"

    @Schema(description = "시설 지역", example = "마포구", defaultValue = "마포구")
    private String area; // 시설 지역, 기본값은 "마포구"

    @Schema(description = "시설 가격 유형(무료/유료)", example = "true", defaultValue = "true")
    private Boolean priceType; // 무료/유료 여부, 기본값은 true (유료)

    @Schema(description = "좋아요 정렬 기준 (0: 정렬 없음, 1: 내림차순, 2: 오름차순)", example = "1", defaultValue = "0")
    private Integer likeOrder=0; // 좋아요 정렬 기준

    // 조회수와 좋아요 순으로 정렬에 대한 필드는 추후 추가할 수 있습니다.
}
