package backend.dev.facility.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class FacilityBase {

    @Id
    private String facilityId; // 시설 고유 ID
    private String facilityName; // 시설 이름

    @Enumerated(EnumType.STRING)
    @Column(name = "category",nullable = false)
    private FacilityCategory facilityCategory; // 시설 카테고리 (e.g., 테니스장, 체육관)

    private String area; // 시설 지역

    private String facilityImage; // 이미지 URL (이미지를 Base64로 변환하거나 URL로 저장)

    private Boolean priceType; // 무료(true) / 유료(false)

    private Point location; // 좌표 (latitude, longitude)

    private LocalDateTime reservationStartDate; //접수 시작일
    private LocalDateTime reservationEndDate; // 접수 마감일

    // 공통 메서드
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

    public void changeLocation(Point location) {
        this.location = location;
    }
}
