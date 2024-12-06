package backend.dev.facility.repository;

import backend.dev.facility.dto.FacilityFilterDTO;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.exception.FacilityException;
import backend.dev.testdata.FacilityInitializer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.test.context.TestPropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Import(FacilityInitializer.class) // FacilityInitializer를 테스트 클래스에 임포트
@Transactional()
public class FacilityDetailsRepositoryTests {

    @Autowired
    private FacilityDetailsRepository facilityDetailsRepository;

    @Test
    @DisplayName("FAC1 ID를 가진 시설 조회 테스트")
    public void testFindFacilityById() {
        // given: 테스트 데이터에 "FAC1" ID를 가진 시설이 존재한다고 가정
        String facilityId = "FAC1";

        // when: 해당 ID로 시설을 조회
        FacilityDetails facility = facilityDetailsRepository.findById(facilityId).orElseThrow(FacilityException.FACILITY_NOT_FOUND::getFacilityTaskException);

    }

    @Test
    @DisplayName("시설 이름으로 검색된 결과가 정확한지 확인하는 테스트")
    public void testFindFacilityByName(@PageableDefault Pageable pageable) {
        // given: 테스트 데이터에 "Facility 1" 이름을 가진 시설이 존재한다고 가정
        String facilityName = "Seoul";
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when: 해당 이름으로 시설 검색
        Page<FacilityDetails> facilities = facilityDetailsRepository.findFacilityByName(facilityName,pageable);

        // then: "Facility 1" 이름을 가진 시설이 검색되어야 한다.
        assertNotNull(facilities, "시설 목록은 null이 아니어야 합니다.");
        assertFalse(facilities.isEmpty(), "시설 목록은 비어있지 않아야 합니다.");
        for (FacilityDetails facility : facilities) {
            assertTrue(facility.getFacilityName().contains(facilityName),"시설 이름에 facilityName이 포함되어 있어야 합니다");
            System.out.println(facility.getArea());
        }
    }

    @Test
    @DisplayName("검색 조건에 맞는 시설들이 정확히 필터링되는지 확인하는 테스트")
    public void testFindFacilityWithCriteria() {
        // given: FacilitySearchCriteriaDTO로 검색 조건을 설정
        FacilityFilterDTO criteria = new FacilityFilterDTO();
        criteria.setPriceType(true);  // 무료 시설만 검색
        criteria.setFacilityCategory("체육관");  // 체육관 시설만 검색
        criteria.setArea("Seoul");  // 대구 지역의 시설만 검색
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when: 검색 조건에 맞는 시설을 찾는다.
        Page<FacilityDetails> facilities = facilityDetailsRepository.findFacility(criteria, pageRequest);

        // then: 검색된 시설은 가격이 무료(true)이고, 시설 카테고리가 "GYM"이어야 한다.
        assertNotNull(facilities, "시설 목록은 null이 아니어야 합니다.");
        facilities.forEach(facility -> {
            assertEquals(FacilityCategory.GYM, facility.getFacilityCategory(),
                    "시설 카테고리는 'GYM'이어야 합니다.");
            assertTrue(facility.getArea().contains("Seoul"), "시설 지역은 'Seoul'을 포함해야 합니다.");
            assertTrue(facility.getPriceType(), "시설은 무료여야 합니다.");
        });
    }
}
