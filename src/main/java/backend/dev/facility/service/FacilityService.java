package backend.dev.facility.service;

import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.dto.facility.FacilityUpdateDTO;
import backend.dev.facility.entity.Facility;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.repository.FacilityRepository;
import backend.dev.utils.PagingVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityService {
    PagingVO pagingVO = new PagingVO();
    Pageable pageable = pagingVO.getPageable();

    private final FacilityRepository facilityRepository;

    // 특정 Facility 조회 -> FacilityResponseDTO 반환
    public FacilityResponseDTO getFacilityById(String id) {
        Facility facility = facilityRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Facility not found with id: " + id));
        return FacilityResponseDTO.fromEntity(facility);
    }

    // 새로운 Facility 추가 -> FacilityResponseDTO 반환
    public FacilityResponseDTO addFacility(Facility facility) {
        Facility savedFacility = facilityRepository.save(facility);
        return FacilityResponseDTO.fromEntity(savedFacility);
    }

    // 여러 Facility 추가 -> List<FacilityResponseDTO> 반환
    public List<FacilityResponseDTO> addFacilities(List<Facility> facilities) {
        return facilityRepository.saveAll(facilities).stream()
                .map(FacilityResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Facility 업데이트 -> FacilityResponseDTO 반환
    public FacilityResponseDTO updateFacility(FacilityUpdateDTO updateDTO, String id) {
        Facility facility = facilityRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Facility not found with id: " + id));

        facility.changeFacilityName(updateDTO.getFacilityName());
        facility.changeArea(updateDTO.getArea());
        facility.changeFacilityCategory(updateDTO.getFacilityCategory());

        Facility updatedFacility = facilityRepository.save(facility);
        return FacilityResponseDTO.fromEntity(updatedFacility);
    }
    //카테고리로 시설 찾기
    public Page<FacilityResponseDTO> getFacilitiesByCategory(FacilityCategory facilityCategory) {

        Page<Facility> byFacilityCategory = facilityRepository.findByFacilityCategory(facilityCategory, pageable);
        return byFacilityCategory.map(FacilityResponseDTO::fromEntity);
    }

    // Facility 삭제
    public void deleteFacility(String id) {
        if (!facilityRepository.existsById(id)) {
            throw new IllegalArgumentException("Facility not found with id: " + id);
        }
        facilityRepository.deleteById(id);
    }
}
