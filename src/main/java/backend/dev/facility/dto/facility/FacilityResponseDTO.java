package backend.dev.facility.dto.facility;

import backend.dev.facility.dto.FacilityBaseDTO;
import backend.dev.facility.entity.Facility;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder

@AllArgsConstructor
public class FacilityResponseDTO extends FacilityBaseDTO {
    // 추가 필드 없음

    public static FacilityResponseDTO fromEntity(Facility facility) {
        return FacilityResponseDTO.builder()
                .facilityId(facility.getFacilityId())
                .facilityName(facility.getFacilityName())
                .facilityCategory(facility.getFacilityCategory())
                .area(facility.getArea())
                .priceType(facility.getPriceType())
                .build();
    }
}
