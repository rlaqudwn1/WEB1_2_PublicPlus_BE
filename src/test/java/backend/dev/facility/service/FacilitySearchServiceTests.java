package backend.dev.facility.service;

import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.entity.Facility;
import backend.dev.facility.exception.FacilityException;
import backend.dev.facility.repository.FacilityRepository;
import backend.dev.testdata.FacilityInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Import(FacilityInitializer.class) // FacilityInitializer를 테스트 클래스에 임포트
public class FacilitySearchServiceTests {

    @Autowired
    private FacilityRepository facilityRepository; // FacilityRepository를 주입받아 데이터를 검색하거나 검증할 수 있음

    @Autowired
    private FacilityService facilityService;

    @BeforeEach
    public void setUp() {
    }
    @Test
    public void testGetFacilityById_Success() {
        // given
        String id = "FAC1";
        Facility facility = facilityRepository.findById(id).orElseThrow(FacilityException.FACILITY_NOT_FOUND::getFacilityTaskException);

        // when
        FacilityResponseDTO response = facilityService.getFacilityById(id);

        // then
        assertNotNull(response);
        assertEquals(facility.getFacilityName(), response.getFacilityName());
    }



}
