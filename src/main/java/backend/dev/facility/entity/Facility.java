package backend.dev.facility.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@EntityListeners(AuditingEntityListener.class)
@ToString
@Entity
@Builder
@Table(name = "facility")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Facility {

        @Id
        @Column(name = "facility_id", nullable = false)
        private String id; // 시설 고유 ID

        private String facilityName; // 시설 이름

        @Enumerated(EnumType.STRING)
        @Column(name = "category", nullable = false)
        private FacilityCategory facilityCategory; // 시설 카테고리

        private String area; // 시설 지역

        private String facilityImage; // 이미지 URL

        private Boolean priceType; // 무료(true) / 유료(false)

        private Float latitude;
        private Float longitude;

        private LocalDateTime reservationStartDate; // 접수 시작일
        private LocalDateTime reservationEndDate;   // 접수 마감일

        public void changeFacilityId(String id) {
                this.id = id;
        }

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

        public void changeLocation(Float latitude , Float longitude) {
                this.latitude = latitude;
                this.longitude = longitude;
        }
}

