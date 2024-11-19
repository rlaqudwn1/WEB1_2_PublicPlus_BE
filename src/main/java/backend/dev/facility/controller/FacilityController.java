package backend.dev.facility.controller;


import backend.dev.facility.entity.Facility;
import backend.dev.facility.service.FacilityAPIService;
import backend.dev.facility.service.FacilityParsingService;
import backend.dev.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/facilites")
public class FacilityController {
    private final FacilityAPIService facilityAPIService;
    private final FacilityParsingService facilityParsingService;
    private final FacilityService facilityService;

    @GetMapping("/{facilityName}")
    public ResponseEntity<?> getFacility(@PathVariable String facilityName) {
        return ResponseEntity.ok(facilityAPIService.fetchSportFacilityData(facilityName));
    }
    @PostMapping("/{facilityName}")
    public ResponseEntity<?> facilitySave(@PathVariable String facilityName) {
        String jsonRowData = facilityAPIService.fetchSportFacilityData(facilityName);
        Facility facility = facilityParsingService.parseFacility(jsonRowData);
        return ResponseEntity.ok(facilityService.addFacility(facility));
    }
    @PostMapping("/{facilityName}/list")
    public ResponseEntity<?> facilitySaves(@PathVariable String facilityName) {
        String jsonRowData = facilityAPIService.fetchSportFacilityData(facilityName);
        List<Facility> facilities = facilityParsingService.parseFacilities(jsonRowData);
        return ResponseEntity.ok(facilityService.addFacilities(facilities));
    }

}
