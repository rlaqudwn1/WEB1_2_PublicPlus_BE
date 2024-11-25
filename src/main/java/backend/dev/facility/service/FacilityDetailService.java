package backend.dev.facility.service;

import backend.dev.facility.dto.facilitydetails.FacilityDetailsResponseDTO;
import backend.dev.facility.dto.facilitydetails.FacilityDetailsUpdateDTO;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.repository.FacilityDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityDetailService {

    private final FacilityDetailsRepository facilityDetailsRepository;

    // FacilityDetails 추가 -> List<FacilityDetailsResponseDTO> 반환
    public List<FacilityDetailsResponseDTO> addFacilityDetailsList(List<FacilityDetails> facilityDetails) {
        return facilityDetailsRepository.saveAll(facilityDetails).stream()
                .map(FacilityDetailsResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // FacilityDetails 업데이트 -> FacilityDetailsResponseDTO 반환
    public FacilityDetailsResponseDTO updateFacilityDetails(FacilityDetailsUpdateDTO updateDTO, String id) {
        FacilityDetails facilityDetails = facilityDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FacilityDetails not found with id: " + id));

        // 각 필드를 업데이트할 때, 값이 존재하는 경우만 업데이트
        updateIfPresent(updateDTO.getFacilityImage(), facilityDetails::changeFacilityImage);
        updateIfPresent(updateDTO.getFacilityNumber(), facilityDetails::changeFacilityNumber);
        updateIfPresent(updateDTO.getReservationURL(), facilityDetails::changeReservationURL);
        updateIfPresent(updateDTO.getFacilityLocation(), facilityDetails::changeFacilityLocation);
        updateIfPresent(updateDTO.getFacilityDescription(), facilityDetails::changeFacilityDescription);

        // 저장
        FacilityDetails updatedFacilityDetails = facilityDetailsRepository.save(facilityDetails);

        // DTO로 변환하여 반환
        return FacilityDetailsResponseDTO.fromEntity(updatedFacilityDetails);
    }

    // 값을 업데이트할 때만 실행하는 메소드
    private <T> void updateIfPresent(T value, Consumer<T> updateFunction) {
        if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
            updateFunction.accept(value);
        }
    }

    //

    // FacilityDetails 삭제
    public void deleteFacilityDetails(String id) {
        if (!facilityDetailsRepository.existsById(id)) {
            throw new IllegalArgumentException("FacilityDetails not found with id: " + id);
        }
        facilityDetailsRepository.deleteById(id);
    }
    public FacilityDetailsResponseDTO getFacilityDetails(String id) {
        return FacilityDetailsResponseDTO.fromEntity(facilityDetailsRepository.findById(id).orElseThrow());
    }

    public boolean deleteFacilityDetail(String facilityId) {
        facilityDetailsRepository.deleteById(facilityId);
        return !facilityDetailsRepository.existsById(facilityId);
    }
    // FacilityDetails 모두 삭제
    public void deleteAllFacilityDetails(){
        facilityDetailsRepository.deleteAll();
    }
}
