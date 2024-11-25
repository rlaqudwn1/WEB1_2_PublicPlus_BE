package backend.dev.facility.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityFilterDTO {
    private String facilityName;
    private String facilityCategory; // 시설 카테고리
    private String area; // 시설 지역
    private Boolean priceType; // 무료/유료 여부
    // 조회수와
    // 좋아요 순으로 정렬에
}
