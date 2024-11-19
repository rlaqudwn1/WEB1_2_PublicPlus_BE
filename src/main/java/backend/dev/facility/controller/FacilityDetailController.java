package backend.dev.facility.controller;


import backend.dev.facility.dto.facilitydetails.FacilityDetailsResponseDTO;
import backend.dev.facility.entity.Facility;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.service.FacilityAPIService;
import backend.dev.facility.service.FacilityDetailService;
import backend.dev.facility.service.FacilityParsingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/{facilityName}/list")
    public ResponseEntity<?> addFacilityDetails(@PathVariable String facilityName){

        String jsonRawData = facilityAPIService.fetchSportFacilityData(facilityName);
        List<FacilityDetails> facilityDetails = facilityParsingService.parseFacilityDetailsList(jsonRawData);
        List<FacilityDetailsResponseDTO> facilityDetailsResponseDTOS = facilityDetailService.addFacilityDetailsList(facilityDetails);
        return ResponseEntity.ok(facilityDetailsResponseDTOS);
    }

}
