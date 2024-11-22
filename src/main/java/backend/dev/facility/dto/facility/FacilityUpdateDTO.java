package backend.dev.facility.dto.facility;

import backend.dev.facility.dto.FacilityBaseDTO;
import backend.dev.facility.entity.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityUpdateDTO extends FacilityBaseDTO {

    private String facilityImage; // 이미지 URL (변경 가능)
    private Point location; // 좌표 (변경 가능)
}
