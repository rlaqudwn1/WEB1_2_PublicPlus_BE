package backend.dev.facility.controller;


import backend.dev.facility.dto.FacilitySearchCriteriaDTO;
import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.entity.Facility;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.service.FacilityAPIService;
import backend.dev.facility.service.FacilityParsingService;
import backend.dev.facility.service.FacilitySearchService;
import backend.dev.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/facilites")
public class FacilityController {
    private final FacilityAPIService facilityAPIService;
    private final FacilityParsingService facilityParsingService;
    private final FacilityService facilityService;
    private final FacilitySearchService facilitySearchService;

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

    @GetMapping("/{facilityCategory}/list")
    public ResponseEntity<?> facilityCategories(@PathVariable String facilityCategory) {

        return ResponseEntity.ok(facilitySearchService.getFacilitiesByCategory(FacilityCategory.fromName(facilityCategory)));
    }
    @GetMapping("/filter")
    public ResponseEntity<?> facilityFilter(@RequestBody FacilitySearchCriteriaDTO facilitySearchCriteriaDTO) {

        return ResponseEntity.ok(facilitySearchService.getFacilitiesByFilter(facilitySearchCriteriaDTO));
    }
    @PostMapping("/all-memory")
    public ResponseEntity<?> facilityAllMemory() {
        List<Facility> facilities = new ArrayList<>();
        for (FacilityCategory value : FacilityCategory.values()) {
            String facilityData = facilityAPIService.fetchSportFacilityData(value.getName());
            facilities.addAll(facilityParsingService.parseFacilities(facilityData));
            facilityService.addFacilities(facilities);
        }
        if (facilities.size() > 400) {
            return ResponseEntity.status(200).body(Map.of("데이터 변환:", "성공"));
        }else return ResponseEntity.status(400).body(Map.of("데이터 변환:","실패"));
    }
}
