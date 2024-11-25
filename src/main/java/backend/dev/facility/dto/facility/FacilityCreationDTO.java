package backend.dev.facility.dto.facility;

import backend.dev.facility.dto.FacilityBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityCreationDTO extends FacilityBaseDTO {

    private String facilityImage; // 이미지 URL
    private Float latitude; // 좌표 (latitude, longitude)
    private Float longitude;
}
