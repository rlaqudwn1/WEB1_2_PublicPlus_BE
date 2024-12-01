package backend.dev.facility.dto.facility;

import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder

@AllArgsConstructor
public class FacilityResponseDTO {
    @Schema(description = "시설 ID", example = "S241010135430829736")
    private String facilityId; // 시설 고유 ID
    @Schema(description = "시설 이름", example = "서울대공원 리틀야구장 2024. 11월(추첨)")
    private String facilityName; // 시설 이름
    @Schema(description = "시설 카테고리", example = "BASEBALL_FIELD")
    private FacilityCategory facilityCategory; // 시설 카테고리
    @Schema(description = "시설 지역", example = "과천시")
    private String area; // 시설 지역
    @Schema(description = "시설 가격 정보", example = "false")
    private Boolean priceType; // 무료(true) / 유료(false)
    @Schema(description = "시설 이미지 URL", example = "https://yeyak.seoul.go.kr/web/common/file/FileDown.do?file_id=1728536177983WXNLQGCX4V5595HK8AC80ADS8")
    private String facilityImage;
    @Schema(description = "예약 시작 날짜", example = "2024-10-14T10:00:00")
    private LocalDateTime reservationStartDate; // 서비스 시작 날짜
    @Schema(description = "예약 종료 날짜", example = "2024-10-16T16:00:00")
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