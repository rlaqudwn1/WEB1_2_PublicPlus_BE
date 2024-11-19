package backend.dev.facility.dto.facilitydetails;


import backend.dev.facility.dto.FacilityBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDetailsCreateDTO extends FacilityBaseDTO {
    private String facilityImage; // 이미지 URL
    private String facilityNumber; // 전화번호
    private String reservationURL; // 예약 URL
    private String facilityLocation; // 상세 주소
    private String facilityDescription; // 시설 설명
}
