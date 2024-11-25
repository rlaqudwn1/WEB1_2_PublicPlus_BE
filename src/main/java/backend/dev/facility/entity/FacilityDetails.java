package backend.dev.facility.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "facility_details",
        indexes = {
                // 필요한 인덱스 추가
                @Index(name = "facilityCategory", columnList = "category"),
                @Index(name = "area", columnList = "area"),
                @Index(name = "priceType", columnList = "priceType")
        })
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityDetails {

    @Id
    @Column(name = "facility_id", nullable = false)
    private String facilityId; // 시설 고유 ID

    @Column(name = "facility_name", nullable = false)
    private String facilityName; // 시설 이름

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private FacilityCategory facilityCategory; // 시설 카테고리 (e.g., 테니스장, 체육관)

    private String area; // 시설 지역

    private String facilityImage; // 이미지 URL

    private Boolean priceType; // 무료(true) / 유료(false)

    private Float latitude; // 좌표 (위도)
    private Float longitude; // 좌표 (경도)

    private LocalDateTime reservationStartDate; // 접수 시작일
    private LocalDateTime reservationEndDate;   // 접수 마감일

    private String facilityNumber; // 전화번호
    private String reservationURL; // 예약 URL
    private String facilityLocation; // 상세 주소

    @Lob
    private String facilityDescription; // 시설 설명

    private String serviceStartDate; // 서비스 시작 날짜
    private String serviceEndDate;   // 서비스 종료 날짜

    // 필드 변경 메서드
    public void changeFacilityCategory(FacilityCategory category) {
        this.facilityCategory = category;
    }

    public void changeFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public void changeArea(String area) {
        this.area = area;
    }

    public void changeFacilityImage(String facilityImage) {
        this.facilityImage = facilityImage;
    }

    public void changePriceType(Boolean priceType) {
        this.priceType = priceType;
    }

    public void changeLocation(Float latitude, Float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void changeFacilityNumber(String facilityNumber) {
        this.facilityNumber = facilityNumber;
    }

    public void changeReservationURL(String reservationURL) {
        this.reservationURL = reservationURL;
    }

    public void changeFacilityLocation(String facilityLocation) {
        this.facilityLocation = facilityLocation;
    }

    public void changeFacilityDescription(String facilityDescription) {
        this.facilityDescription = facilityDescription;
    }

    public void changeServiceStartDate(String serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    public void changeServiceEndDate(String serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }
}
