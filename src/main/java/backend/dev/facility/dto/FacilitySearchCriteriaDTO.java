package backend.dev.facility.dto;
import backend.dev.facility.entity.FacilityCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilitySearchCriteriaDTO {

    private String facilityCategory; // 시설 카테고리
    private String area; // 시설 지역
    private Boolean priceType; // 무료/유료 여부
}
