package backend.dev.facility.dto;

import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.entity.Facility;
import backend.dev.facility.entity.FacilityCategory;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@MappedSuperclass
public abstract class FacilityBaseDTO {
    private String facilityId; // 시설 고유 ID
    private String facilityName; // 시설 이름
    private FacilityCategory facilityCategory; // 시설 카테고리
    private String area; // 시설 지역
    private Boolean priceType; // 무료(true) / 유료(false)
    private String facilityImage;
    private LocalDateTime reservationStartDate; // 서비스 시작 날짜
    private LocalDateTime reservationEndDate; // 서비스 종료 날짜
}
