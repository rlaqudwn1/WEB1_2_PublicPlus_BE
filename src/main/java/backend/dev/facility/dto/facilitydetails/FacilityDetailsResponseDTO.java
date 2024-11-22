package backend.dev.facility.dto.facilitydetails;

import backend.dev.facility.dto.FacilityBaseDTO;
import backend.dev.facility.entity.FacilityDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDetailsResponseDTO extends FacilityBaseDTO {
    private String facilityImage; // 이미지 URL
    private String facilityNumber; // 전화번호
    private String reservationURL; // 예약 URL
    private String facilityLocation; // 상세 주소
    private String facilityDescription; // 시설 설명


    public static FacilityDetailsResponseDTO fromEntity(FacilityDetails facilityDetails) {
        return FacilityDetailsResponseDTO.builder()
                .facilityId(facilityDetails.getFacilityId())
                .facilityName(facilityDetails.getFacilityName())
                .facilityCategory(facilityDetails.getFacilityCategory())
                .area(facilityDetails.getArea())
                .priceType(facilityDetails.getPriceType())
                .serviceStartDate(facilityDetails.getServiceStartDate())
                .serviceEndDate(facilityDetails.getServiceEndDate())
                .facilityImage(facilityDetails.getFacilityImage())
                .facilityNumber(facilityDetails.getFacilityNumber())
                .reservationURL(facilityDetails.getReservationURL())
                .facilityLocation(facilityDetails.getFacilityLocation())
                .facilityDescription(facilityDetails.getFacilityDescription())
                .build();
    }

}
