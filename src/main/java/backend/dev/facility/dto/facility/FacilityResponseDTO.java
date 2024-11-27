package backend.dev.facility.dto.facility;

import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder

@AllArgsConstructor
public class FacilityResponseDTO {
    private String facilityId; // 시설 고유 ID
    private String facilityName; // 시설 이름
    private FacilityCategory facilityCategory; // 시설 카테고리
    private String area; // 시설 지역
    private Boolean priceType; // 무료(true) / 유료(false)
    private String facilityImage;
    private LocalDateTime reservationStartDate; // 서비스 시작 날짜
    private LocalDateTime reservationEndDate; // 서비스 종료 날짜
    // 추가 필드 없음

    public static FacilityResponseDTO fromEntity(FacilityDetails facilityDetails) {
        return FacilityResponseDTO.builder()
                .facilityId(facilityDetails.getFacilityId())
                .facilityName(facilityDetails.getFacilityName())
                .facilityCategory(facilityDetails.getFacilityCategory())
                .area(facilityDetails.getArea())
                .facilityImage(facilityDetails.getFacilityImage())
                .reservationStartDate(facilityDetails.getReservationStartDate())
                .reservationEndDate(facilityDetails.getReservationEndDate())
                .priceType(facilityDetails.getPriceType())
                .build();
    }
}
