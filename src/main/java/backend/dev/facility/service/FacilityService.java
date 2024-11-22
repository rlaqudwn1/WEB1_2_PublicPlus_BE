package backend.dev.facility.service;

import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.dto.facility.FacilityUpdateDTO;
import backend.dev.facility.entity.Facility;
import backend.dev.facility.exception.FacilityException;
import backend.dev.facility.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityService {


    private final FacilityRepository facilityRepository;
    private final Pageable defaultPageable;

    // 특정 Facility 조회 -> FacilityResponseDTO 반환
    public FacilityResponseDTO getFacilityById(String id) {
        Facility facility = facilityRepository.findById(id).orElseThrow(FacilityException.FACILITY_NOT_FOUND::getFacilityTaskException);
        return FacilityResponseDTO.fromEntity(facility);
    }

    // 새로운 Facility 추가 -> FacilityResponseDTO 반환
    public FacilityResponseDTO addFacility(Facility facility) {
        try {
            Facility savedFacility = facilityRepository.save(facility);
            return FacilityResponseDTO.fromEntity(savedFacility);
        } catch (Exception e) {
            throw FacilityException.FACILITY_ADD_FAILED.getFacilityTaskException();
        }
    }

    // 여러 Facility 추가 -> List<FacilityResponseDTO> 반환
    public List<FacilityResponseDTO> addFacilities(List<Facility> facilities) {
        try {
            return facilityRepository.saveAll(facilities).stream()
                    .map(FacilityResponseDTO::fromEntity)
                    .collect(Collectors.toList());
        }catch (Exception e) {
            throw FacilityException.FACILITY_ADD_FAILED.getFacilityTaskException();
        }

    }

    // Facility 업데이트 -> FacilityResponseDTO 반환
    public FacilityResponseDTO updateFacility(FacilityUpdateDTO updateDTO, String id) {
        //given 업데이트할 데이터가 Validate를 거쳐서 왔을 경우
        Facility facility = facilityRepository.findById(id).orElseThrow(FacilityException.FACILITY_NOT_FOUND::getFacilityTaskException);
        //when 업데이트 실행
        try {
            // 사용자가 입력한 값이 null이 아니고 빈 문자열이 아닐 경우에만 변경
            if (updateDTO.getFacilityName() != null && !updateDTO.getFacilityName().isEmpty()) {
                facility.changeFacilityName(updateDTO.getFacilityName());
            }

            if (updateDTO.getArea() != null && !updateDTO.getArea().isEmpty()) {
                facility.changeArea(updateDTO.getArea());
            }

            if (updateDTO.getFacilityCategory() != null){
                facility.changeFacilityCategory(updateDTO.getFacilityCategory());
            }
        }catch (Exception e) {
            throw FacilityException.NOT_MODIFIED.getFacilityTaskException();
        }
        //then 업데이트된 데이터 저장 및 return
        Facility updatedFacility = facilityRepository.save(facility);
        return FacilityResponseDTO.fromEntity(updatedFacility);
    }




    // Facility 삭제
    public void deleteFacility(String id) {
       facilityRepository.deleteById(id);
       if (facilityRepository.existsById(id)) {
           throw FacilityException.NOT_DELETED.getFacilityTaskException();
       }
    }

}
