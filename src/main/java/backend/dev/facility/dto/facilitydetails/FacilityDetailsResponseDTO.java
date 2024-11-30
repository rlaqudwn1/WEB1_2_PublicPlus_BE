package backend.dev.facility.dto.facilitydetails;

import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDetailsResponseDTO {

    @Schema(description = "시설 고유 ID", example = "S210401100008601453")
    private String facilityId;

    @Schema(description = "시설 이름", example = "○ [평일] (주간) 마포 난지천 인조잔디축구장")
    private String facilityName;

    @Schema(description = "시설 카테고리", example = "FOOTBALL_FIELD")
    private FacilityCategory facilityCategory;

    @Schema(description = "시설 지역", example = "마포구")
    private String area;

    @Schema(description = "무료/유료 여부", example = "true")
    private Boolean priceType;

    @Schema(description = "시설 이미지 URL", example = "https://example.com/image.jpg")
    private String facilityImage;

    @Schema(description = "예약 시작 날짜", example = "2024-01-30T00:00:00")
    private LocalDateTime reservationStartDate;

    @Schema(description = "예약 종료 날짜", example = "2024-11-30T23:59:00")
    private LocalDateTime reservationEndDate;

    @Schema(description = "시설 전화번호", example = "02-3153-9874")
    private String facilityNumber;

    @Schema(description = "시설 예약 URL", example = "https://example.com/reserve")
    private String reservationURL;

    @Schema(description = "시설 상세 주소", example = "서울특별시 산악문화체험센터>난지천인조잔디축구장")
    private String facilityLocation;

    @Schema(description = "시설 설명", example = "1. 공공시설 예약서비스 이용시 필수 준수사항...")
    private String facilityDescription;

    @Schema(description = "서비스 시작 날짜", example = "2024-01-01")
    private String serviceStartDate;

    @Schema(description = "서비스 종료 날짜", example = "2024-12-31")
    private String serviceEndDate;
    @Schema(description = "위도" , example = "37.5741")
    private Double latitude;
    @Schema(description = "경도" , example = "126.884")
    private Double longitude;

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
                .reservationStartDate(facilityDetails.getReservationStartDate())
                .reservationEndDate(facilityDetails.getReservationEndDate())
                .reservationURL(facilityDetails.getReservationURL())
                .serviceStartDate(String.valueOf(facilityDetails.getServiceStartDate()))
                .serviceEndDate(String.valueOf(facilityDetails.getServiceEndDate()))
                .facilityLocation(facilityDetails.getFacilityLocation())
                .facilityDescription(facilityDetails.getFacilityDescription())
                .build();
    }
}