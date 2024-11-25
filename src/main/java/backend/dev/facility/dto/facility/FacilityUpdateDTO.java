package backend.dev.facility.dto.facility;

import backend.dev.facility.dto.FacilityBaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityUpdateDTO extends FacilityBaseDTO {

    private Float latitude;
    private Float longitude;
}
