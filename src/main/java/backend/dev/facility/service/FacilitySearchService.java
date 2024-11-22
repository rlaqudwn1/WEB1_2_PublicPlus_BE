package backend.dev.facility.service;

import backend.dev.facility.dto.FacilitySearchCriteriaDTO;
import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.entity.Facility;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.exception.FacilityException;
import backend.dev.facility.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FacilitySearchService {
    private final FacilityRepository facilityRepository;

    private final Pageable defaultPageable;

    //카테고리로 시설 찾기
    public Page<FacilityResponseDTO> getFacilitiesByCategory(FacilityCategory facilityCategory) {
        //when 카테고리를 받았을 경우
        try {
            Page<Facility> byFacilityCategory = facilityRepository.findByFacilityCategory(facilityCategory, defaultPageable);
            //t
            return byFacilityCategory.map(FacilityResponseDTO::fromEntity);
        }catch (Exception e) {
            e.printStackTrace();
            throw FacilityException.INVALID_CATEGORY.getFacilityTaskException();
        }
    }
    //     필터로 시설 찾기
    public Page<FacilityResponseDTO> getFacilitiesByFilter(FacilitySearchCriteriaDTO facilitySearchCriteriaDTO) {
        try {
            return facilityRepository.findFacility(facilitySearchCriteriaDTO,defaultPageable).map(FacilityResponseDTO::fromEntity);
        } catch (Exception e) {
            throw FacilityException.FACILITY_NOT_FOUND.getFacilityTaskException();
        }
    }
}
