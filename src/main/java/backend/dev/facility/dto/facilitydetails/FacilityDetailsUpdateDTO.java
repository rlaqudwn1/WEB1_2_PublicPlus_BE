package backend.dev.facility.dto.facilitydetails;

import backend.dev.facility.entity.FacilityCategory;
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
public class FacilityDetailsUpdateDTO {

    @Schema(description = "시설 고유 ID", example = "S210401100008601453")
    private String facilityId; // 시설 고유 ID

    @Schema(description = "시설 이름", example = "○ [평일] (주간) 마포 난지천 인조잔디축구장 (업데이트)")
    private String facilityName; // 시설 이름

    @Schema(description = "시설 카테고리", example = "FOOTBALL") // 예시로 축구장 카테고리 넣음
    private FacilityCategory facilityCategory; // 시설 카테고리

    @Schema(description = "시설 지역", example = "마포구")
    private String area; // 시설 지역

    @Schema(description = "무료(true) / 유료(false)", example = "true")
    private Boolean priceType; // 무료(true) / 유료(false)

    @Schema(description = "시설 이미지 URL", example = "http://example.com/facility.jpg")
    private String facilityImage; // 시설 이미지

    @Schema(description = "서비스 시작 날짜", example = "2024-01-01T00:00:00")
    private LocalDateTime reservationStartDate; // 서비스 시작 날짜

    @Schema(description = "서비스 종료 날짜", example = "2024-12-31T23:59:59")
    private LocalDateTime reservationEndDate; // 서비스 종료 날짜

    @Schema(description = "시설 전화번호", example = "02-3153-9874")
    private String facilityNumber; // 전화번호

    @Schema(description = "예약 URL", example = "http://example.com/reservation")
    private String reservationURL; // 예약 URL

    @Schema(description = "시설 상세 주소", example = "서울특별시 산악문화체험센터>난지천인조잔디축구장")
    private String facilityLocation; // 상세 주소

    @Schema(description = "시설 설명", example = "1. 공공시설 예약서비스 이용시 필수 준수사항...")
    private String facilityDescription; // 시설 설명

    @Schema(description = "서비스 시작 날짜 (String 형식)", example = "2024-01-01")
    private String serviceStartDate; // 서비스 시작 날짜 (String 형식)

    @Schema(description = "서비스 종료 날짜 (String 형식)", example = "2024-12-31")
    private String serviceEndDate; // 서비스 종료 날짜 (String 형식)
}
