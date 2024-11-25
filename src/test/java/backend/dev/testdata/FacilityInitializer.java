package backend.dev.testdata;

import backend.dev.facility.entity.Facility;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.Point;
import backend.dev.facility.repository.FacilityRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class FacilityInitializer {

    @Autowired
    private FacilityRepository facilityRepository; // 실제 repository를 주입받습니다.

    @PostConstruct
    public void initTestData() {
        // 데이터 생성
        for (int i = 0; i < 50; i++) {
            Facility facility = createTestFacility(i);
            facilityRepository.save(facility);
        }
    }

    private Facility createTestFacility(int index) {
        Random random = new Random();

        // 임의의 데이터를 생성
        FacilityCategory[] categories = FacilityCategory.values();
        FacilityCategory randomCategory = categories[random.nextInt(categories.length)];
        String[] areas = new String[] {"Seoul","Deagu","Busan","Incheon","Ulsan"};

        return Facility.builder()
                .facilityId("FAC" + index)
                .facilityName("Facility " + areas[random.nextInt(areas.length)])
                .facilityCategory(randomCategory)
                .area(areas[random.nextInt(areas.length)]) // 예시로 5개의 지역
                .facilityImage("https://example.com/image" + index + ".jpg")
                .priceType(random.nextBoolean())
                .location(new Point(random.nextDouble(), random.nextDouble())) // 임의의 좌표
                .reservationStartDate(LocalDateTime.now().plusDays(random.nextInt(30))) // 예약 시작일 (현재부터 30일 내)
                .reservationEndDate(LocalDateTime.now().plusDays(random.nextInt(30) + 30)) // 예약 마감일 (시작일부터 30일 후)
                .build();
    }
}
