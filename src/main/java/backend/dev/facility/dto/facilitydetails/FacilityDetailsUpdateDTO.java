package backend.dev.facility.dto.facilitydetails;

import backend.dev.facility.dto.FacilityBaseDTO;
import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.entity.Facility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDetailsUpdateDTO extends FacilityBaseDTO {
    private String facilityImage; // 이미지 URL
    private String facilityNumber; // 전화번호
    private String reservationURL; // 예약 URL
    private String facilityLocation; // 상세 주소
    private String facilityDescription; // 시설 설명


}
