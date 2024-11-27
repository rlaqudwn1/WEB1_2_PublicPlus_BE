package backend.dev.facility.dto.facilitydetails;

import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDetailsResponseDTO {

    @Schema(description = "시설 고유 ID", example = "S120423174310156240")
    private String facilityId; // 시설 고유 ID

    @Schema(description = "시설 이름", example = "서울 체육관")
    private String facilityName; // 시설 이름

    @Schema(description = "시설 카테고리", example = "GYM")
    private FacilityCategory facilityCategory; // 시설 카테고리

    @Schema(description = "시설 지역", example = "서울시 강남구")
    private String area; // 시설 지역

    @Schema(description = "무료/유료 여부 (true: 무료, false: 유료)", example = "false")
    private Boolean priceType; // 무료(true) / 유료(false)

    @Schema(description = "시설 이미지 URL", example = "https://example.com/image.jpg")
    private String facilityImage;

    @Schema(description = "서비스 시작 날짜", example = "2024-01-01T00:00:00")
    private LocalDateTime reservationStartDate; // 서비스 시작 날짜

    @Schema(description = "서비스 종료 날짜", example = "2024-12-31T23:59:59")
    private LocalDateTime reservationEndDate; // 서비스 종료 날짜

    @Schema(description = "시설 전화번호", example = "02-1234-5678")
    private String facilityNumber; // 전화번호

    @Schema(description = "시설 예약 URL", example = "https://example.com/reserve")
    private String reservationURL; // 예약 URL

    @Schema(description = "시설 상세 주소", example = "서울시 강남구 테헤란로 123")
    private String facilityLocation; // 상세 주소

    @Schema(description = "시설 설명", example = "이 시설은 다양한 스포츠 활동을 제공하는 현대적인 체육관입니다.")
    private String facilityDescription; // 시설 설명

    @Schema(description = "서비스 시작 날짜", example = "2024-01-01")
    private String serviceStartDate;

    @Schema(description = "서비스 종료 날짜", example = "2024-12-31")
    private String serviceEndDate;

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
