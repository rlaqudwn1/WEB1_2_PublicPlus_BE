package backend.dev.facility.controller;


import backend.dev.facility.dto.facilitydetails.FacilityDetailsResponseDTO;
import backend.dev.facility.entity.Facility;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.exception.FacilityException;
import backend.dev.facility.exception.FacilityTaskException;
import backend.dev.facility.service.FacilityAPIService;
import backend.dev.facility.service.FacilityDetailService;
import backend.dev.facility.service.FacilityParsingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/facility-detail")
public class FacilityDetailController {
    private final FacilityDetailService facilityDetailService;
    private final FacilityParsingService facilityParsingService;
    private final FacilityAPIService facilityAPIService;

    @GetMapping("/{facilityName}")
    public ResponseEntity<?> getFacilityDetail(@PathVariable String facilityName) {

        return ResponseEntity.ok(facilityAPIService.fetchSportFacilityData(facilityName));
    }

    @GetMapping("")
    public ResponseEntity<?> readFacilityDetails(@RequestParam String facilityId){

        return ResponseEntity.ok(facilityDetailService.getFacilityDetails(facilityId));
    }

    @PostMapping("/{facilityName}/list")
    public ResponseEntity<?> addFacilityDetails(@PathVariable String facilityName){

        String jsonRawData = facilityAPIService.fetchSportFacilityData(facilityName);
        List<FacilityDetails> facilityDetails = facilityParsingService.parseFacilityDetailsList(jsonRawData);
        List<FacilityDetailsResponseDTO> facilityDetailsResponseDTOS = facilityDetailService.addFacilityDetailsList(facilityDetails);
        return ResponseEntity.ok(facilityDetailsResponseDTOS);
    }

    @PostMapping("/all-memory")
    public ResponseEntity<?> addAllFacilityDetails() {
        List<FacilityDetails> facilityDetailsList = new ArrayList<>();
        for (FacilityCategory value : FacilityCategory.values()) {
            String facilityData = facilityAPIService.fetchSportFacilityData(value.getName());
            facilityDetailsList.addAll(facilityParsingService.parseFacilityDetailsList(facilityData));
            facilityDetailService.addFacilityDetailsList(facilityDetailsList);
        }
        if (facilityDetailsList.size() > 400) {
            return ResponseEntity.status(200).body(Map.of("데이터 수집: ", "완료"));
        }
        throw FacilityException.FACILITY_EXCEPTION.getFacilityTaskException();
    }
}
