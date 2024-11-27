package backend.dev.facility.dto.facilitydetails;

import backend.dev.facility.entity.FacilityCategory;
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
    private String facilityId; // 시설 고유 ID
    private String facilityName; // 시설 이름
    private FacilityCategory facilityCategory; // 시설 카테고리
    private String area; // 시설 지역
    private Boolean priceType; // 무료(true) / 유료(false)
    private String facilityImage;
    private LocalDateTime reservationStartDate; // 서비스 시작 날짜
    private LocalDateTime reservationEndDate; // 서비스 종료 날짜
    private String facilityNumber; // 전화번호
    private String reservationURL; // 예약 URL
    private String facilityLocation; // 상세 주소
    private String facilityDescription; // 시설 설명
    private String serviceStartDate;
    private String serviceEndDate;
}
