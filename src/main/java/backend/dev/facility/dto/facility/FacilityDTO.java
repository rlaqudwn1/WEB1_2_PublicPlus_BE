package backend.dev.facility.dto.facility;

import backend.dev.facility.entity.FacilityDetails;

public class FacilityDTO {

    private String facilityId; // 시설 고유 ID
    private String facilityName; // 시설 이름
    private String category; // 시설 카테고리 (Enum을 String으로 변환)
    private String area; // 시설 지역
    private String facilityImage; // 이미지 URL
    private Boolean priceType; // 무료(true) / 유료(false)
    private Double latitude; // 위도
    private Double longitude; // 경도

    // 생성자
    public FacilityDTO(FacilityDetails facilityDetails) {
        this.facilityId = facilityDetails.getFacilityId();
        this.facilityName = facilityDetails.getFacilityName();
        this.category = facilityDetails.getFacilityCategory().name();
        this.area = facilityDetails.getArea();
        this.facilityImage = facilityDetails.getFacilityImage();
        this.priceType = facilityDetails.getPriceType();
        this.latitude = facilityDetails.getLatitude();
        this.longitude = facilityDetails.getLongitude();
    }

    // Getter와 Setter 생략
}