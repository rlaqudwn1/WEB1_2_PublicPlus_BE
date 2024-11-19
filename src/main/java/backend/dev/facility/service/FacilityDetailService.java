package backend.dev.facility.service;

import backend.dev.facility.dto.facilitydetails.FacilityDetailsResponseDTO;
import backend.dev.facility.dto.facilitydetails.FacilityDetailsUpdateDTO;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.repository.FacilityDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        FacilityDetails facilityDetails = facilityDetailsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("FacilityDetails not found with id: " + id));

        facilityDetails.changeFacilityImage(updateDTO.getFacilityImage());
        facilityDetails.changeFacilityNumber(updateDTO.getFacilityNumber());
        facilityDetails.changeReservationURL(updateDTO.getReservationURL());
        facilityDetails.changeFacilityLocation(updateDTO.getFacilityLocation());
        facilityDetails.changeFacilityDescription(updateDTO.getFacilityDescription());

        FacilityDetails updatedFacilityDetails = facilityDetailsRepository.save(facilityDetails);
        return FacilityDetailsResponseDTO.fromEntity(updatedFacilityDetails);
    }

    // FacilityDetails 삭제
    public void deleteFacilityDetails(String id) {
        if (!facilityDetailsRepository.existsById(id)) {
            throw new IllegalArgumentException("FacilityDetails not found with id: " + id);
        }
        facilityDetailsRepository.deleteById(id);
    }
}
