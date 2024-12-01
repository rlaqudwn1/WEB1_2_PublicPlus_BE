package backend.dev.facility.service;

import backend.dev.facility.dto.FacilityFilterDTO;
import backend.dev.facility.dto.facility.FacilityLocationDTO;
import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.exception.FacilityException;
import backend.dev.facility.repository.FacilityDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FacilitySearchService {
    private final FacilityDetailsRepository facilityDetailsRepository;

    private final Pageable defaultPageable;

    //카테고리로 시설 찾기
    public Page<FacilityResponseDTO> getFacilitiesByCategory(FacilityCategory facilityCategory) {
        //when 카테고리를 받았을 경우
        try {
            Page<FacilityDetails> byFacilityCategory = facilityDetailsRepository.findByFacilityCategory(facilityCategory, defaultPageable);

            return byFacilityCategory.map(FacilityResponseDTO::fromEntity);
        }catch (Exception e) {
            e.printStackTrace();
            throw FacilityException.INVALID_CATEGORY.getFacilityTaskException();
        }
    }
    //     필터로 시설 찾기
    public Page<FacilityResponseDTO> getFacilitiesByFilter(FacilityFilterDTO facilityFilterDTO) {
        try {
            return facilityDetailsRepository.findFacility(facilityFilterDTO,defaultPageable).map(FacilityResponseDTO::fromEntity);
        } catch (Exception e) {
            throw FacilityException.FACILITY_NOT_FOUND.getFacilityTaskException();
        }
    }
    public Page<FacilityResponseDTO> getFacilitiesNearBy(FacilityLocationDTO facilityLocationDTO) {
        Page<FacilityDetails> facilitiesByLocation = facilityDetailsRepository.findFacilitiesByLocation(facilityLocationDTO.getLatitude(), facilityLocationDTO.getLongitude(), facilityLocationDTO.getRadius(), defaultPageable);
        return facilitiesByLocation.map(FacilityResponseDTO::fromEntity);
    }
}
