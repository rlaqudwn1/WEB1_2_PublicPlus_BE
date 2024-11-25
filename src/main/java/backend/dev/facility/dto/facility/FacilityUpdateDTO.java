package backend.dev.facility.dto.facility;

import backend.dev.facility.dto.FacilityBaseDTO;
import backend.dev.facility.entity.Point;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityUpdateDTO extends FacilityBaseDTO {

    private Point location; // 좌표 (변경 가능)
}
