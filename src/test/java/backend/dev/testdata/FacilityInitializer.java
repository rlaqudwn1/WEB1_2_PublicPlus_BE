package backend.dev.testdata;

import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.repository.FacilityDetailsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class FacilityInitializer {

    @Autowired
    private FacilityDetailsRepository facilityDetailsRepository; // 실제 repository를 주입받습니다.

    @PostConstruct
    public void initTestData() {
        // 데이터 생성
        for (int i = 0; i < 10000000; i++) {
            FacilityDetails facility = createTestFacility(i);
            facilityDetailsRepository.save(facility);
        }
    }

    private FacilityDetails createTestFacility(int index) {
        Random random = new Random();

        // 임의의 데이터를 생성
        FacilityCategory[] categories = FacilityCategory.values();
        FacilityCategory randomCategory = categories[random.nextInt(categories.length)];
        String[] areas = new String[] {"Seoul","Deagu","Busan","Incheon","Ulsan"};

        return FacilityDetails.builder()
                .facilityId("FAC" + index)
                .facilityName("Facility " + areas[random.nextInt(areas.length)])
                .facilityCategory(randomCategory)
                .area(areas[random.nextInt(areas.length)]) // 예시로 5개의 지역
                .facilityImage("https://example.com/image" + index + ".jpg")
                .priceType(random.nextBoolean())
                .longitude(random.nextDouble())
                .latitude(random.nextDouble())
                .reservationStartDate(LocalDateTime.now().plusDays(random.nextInt(30))) // 예약 시작일 (현재부터 30일 내)
                .reservationEndDate(LocalDateTime.now().plusDays(random.nextInt(30) + 30)) // 예약 마감일 (시작일부터 30일 후)
                .build();
    }
}
