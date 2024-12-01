package backend.dev.facility.service;

import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.dto.facilitydetails.FacilityDetailsResponseDTO;
import backend.dev.facility.dto.facilitydetails.FacilityDetailsUpdateDTO;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.repository.FacilityDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityDetailService {

    private final FacilityDetailsRepository facilityDetailsRepository;
    private final Pageable defaultPageable;

    // FacilityDetails 추가 -> List<FacilityDetailsResponseDTO> 반환
    public List<FacilityDetailsResponseDTO> addFacilityDetailsList(List<FacilityDetails> facilityDetails) {
        return facilityDetailsRepository.saveAll(facilityDetails).stream()
                .map(FacilityDetailsResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // FacilityDetails 업데이트 -> FacilityDetailsResponseDTO 반환
    public FacilityDetailsResponseDTO updateFacilityDetails(FacilityDetailsUpdateDTO updateDTO, String id) {
        // 먼저 id로 FacilityDetails를 조회
        FacilityDetails facilityDetails = facilityDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FacilityDetails not found with id: " + id));

        // 값이 존재하는 경우만 필드를 업데이트
        updateIfPresent(updateDTO.getFacilityName(), facilityDetails::changeFacilityName);
        updateIfPresent(updateDTO.getFacilityCategory(), facilityDetails::changeFacilityCategory);
        updateIfPresent(updateDTO.getArea(), facilityDetails::changeArea);
        updateIfPresent(updateDTO.getPriceType(), facilityDetails::changePriceType);
        updateIfPresent(updateDTO.getFacilityImage(), facilityDetails::changeFacilityImage);
        updateIfPresent(updateDTO.getReservationStartDate(), facilityDetails::changeReservationStartDate);
        updateIfPresent(updateDTO.getReservationEndDate(), facilityDetails::changeReservationEndDate);
        updateIfPresent(updateDTO.getFacilityNumber(), facilityDetails::changeFacilityNumber);
        updateIfPresent(updateDTO.getReservationURL(), facilityDetails::changeReservationURL);
        updateIfPresent(updateDTO.getFacilityLocation(), facilityDetails::changeFacilityLocation);
        updateIfPresent(updateDTO.getFacilityDescription(), facilityDetails::changeFacilityDescription);
        updateIfPresent(updateDTO.getServiceStartDate(), facilityDetails::changeServiceStartDate);
        updateIfPresent(updateDTO.getServiceEndDate(), facilityDetails::changeServiceEndDate);

        // 업데이트된 FacilityDetails를 저장
        FacilityDetails updatedFacilityDetails = facilityDetailsRepository.save(facilityDetails);

        // 업데이트된 엔티티를 DTO로 변환하여 반환
        return FacilityDetailsResponseDTO.fromEntity(updatedFacilityDetails);
    }

    // 값이 존재할 때만 업데이트하는 helper 메서드
    private <T> void updateIfPresent(T value, Consumer<T> updateMethod) {
        if (value != null) {
            updateMethod.accept(value);  // 값이 null이 아니면 해당 메서드 호출
        }
    }

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

    // 모든 시설 상세 정보 페이지화
    public Page<FacilityDetailsResponseDTO> getAllDetails(){
        return facilityDetailsRepository.findAll(defaultPageable).map(FacilityDetailsResponseDTO::fromEntity);
    }
    public Page<FacilityResponseDTO> getAllFacility(){
        return facilityDetailsRepository.findAll(defaultPageable).map(FacilityResponseDTO::fromEntity);
    }
}
