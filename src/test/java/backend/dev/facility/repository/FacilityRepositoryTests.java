package backend.dev.facility.repository;

import backend.dev.facility.dto.FacilitySearchCriteriaDTO;
import backend.dev.facility.entity.Facility;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.exception.FacilityException;
import backend.dev.testdata.FacilityInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Import(FacilityInitializer.class) // FacilityInitializer를 테스트 클래스에 임포트
public class FacilityRepositoryTests {

    @Autowired
    private FacilityRepository facilityRepository; // FacilityRepository를 주입받아 데이터를 검색하거나 검증할 수 있음

    @BeforeEach
    public void setUp() {
        // 테스트 데이터 초기화
        // 이 메서드는 테스트 시작 전에 호출됩니다. FacilityInitializer에서 데이터가 자동으로 삽입되므로 별도로 호출할 필요는 없음
    }

    @Test
    @DisplayName("FAC1 ID를 가진 시설 조회 테스트")
    public void testFindFacilityById() {
        // given: 테스트 데이터에 "FAC1" ID를 가진 시설이 존재한다고 가정
        String facilityId = "FAC1";

        // when: 해당 ID로 시설을 조회
        Facility facility = facilityRepository.findById(facilityId).orElseThrow(FacilityException.FACILITY_NOT_FOUND::getFacilityTaskException);

    }

    @Test
    @DisplayName("시설 이름으로 검색된 결과가 정확한지 확인하는 테스트")
    public void testFindFacilityByName() {
        // given: 테스트 데이터에 "Facility 1" 이름을 가진 시설이 존재한다고 가정
        String facilityName = "Seoul";
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when: 해당 이름으로 시설 검색
        Page<Facility> facilities = facilityRepository.findFacilityByName(facilityName);

        // then: "Facility 1" 이름을 가진 시설이 검색되어야 한다.
        assertNotNull(facilities, "시설 목록은 null이 아니어야 합니다.");
        assertFalse(facilities.isEmpty(), "시설 목록은 비어있지 않아야 합니다.");
        for (Facility facility : facilities) {
            assertTrue(facility.getFacilityName().contains(facilityName),"시설 이름에 facilityName이 포함되어 있어야 합니다");
            System.out.println(facility.getArea());
        }
    }

    @Test
    @DisplayName("검색 조건에 맞는 시설들이 정확히 필터링되는지 확인하는 테스트")
    public void testFindFacilityWithCriteria() {
        // given: FacilitySearchCriteriaDTO로 검색 조건을 설정
        FacilitySearchCriteriaDTO criteria = new FacilitySearchCriteriaDTO();
        criteria.setPriceType(true);  // 무료 시설만 검색
        criteria.setFacilityCategory("체육관");  // 체육관 시설만 검색
        criteria.setArea("Seoul");  // 대구 지역의 시설만 검색
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when: 검색 조건에 맞는 시설을 찾는다.
        Page<Facility> facilities = facilityRepository.findFacility(criteria, pageRequest);

        // then: 검색된 시설은 가격이 무료(true)이고, 시설 카테고리가 "GYM"이어야 한다.
        assertNotNull(facilities, "시설 목록은 null이 아니어야 합니다.");
        assertFalse(facilities.isEmpty(), "시설 목록은 비어있지 않아야 합니다.");
        facilities.forEach(facility -> {
            assertEquals(FacilityCategory.GYM, facility.getFacilityCategory(),
                    "시설 카테고리는 'GYM'이어야 합니다.");
            assertTrue(facility.getArea().contains("Seoul"), "시설 지역은 'Seoul'을 포함해야 합니다.");
            assertTrue(facility.getPriceType(), "시설은 무료여야 합니다.");
        });
    }
}
