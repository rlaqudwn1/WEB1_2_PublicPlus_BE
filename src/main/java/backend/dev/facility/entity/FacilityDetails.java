package backend.dev.facility.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "facility_details",
        indexes = {
//                @Index(name = "facilityCategory", columnList = "category"),
//                @Index(name = "area", columnList = "area"),
//                @Index(name = "priceType", columnList = "priceType")
        })
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public class FacilityDetails extends FacilityBase {

    private String facilityNumber; // 전화번호
    private String reservationURL; // 예약 URL
    private String facilityLocation; // 상세 주소
    @Lob
    private String facilityDescription; // 시설 설명
    private LocalDateTime serviceStartDate; // 서비스 시작 날짜
    private LocalDateTime serviceEndDate; // 서비스 종료 날짜
    
    // 디테일의 change 메서드
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
    public void changeServiceStartDate(LocalDateTime serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }
    public void changeServiceEndDate(LocalDateTime serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }
}
