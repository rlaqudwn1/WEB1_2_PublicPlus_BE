package backend.dev.facility.controller;

import backend.dev.facility.dto.FacilityFilterDTO;
import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.dto.facility.FacilityUpdateDTO;
import backend.dev.facility.entity.Facility;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.exception.FacilityException; // FacilityException 가져오기
import backend.dev.facility.service.FacilityAPIService;
import backend.dev.facility.service.FacilityParsingService;
import backend.dev.facility.service.FacilitySearchService;
import backend.dev.facility.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "시설 검색정보를 컨트롤하는 API 입니다")
@RequestMapping("api/facilites")
public class FacilityController {
    private final FacilityAPIService facilityAPIService;
    private final FacilityParsingService facilityParsingService;
    private final FacilityService facilityService;
    private final FacilitySearchService facilitySearchService;

    @Operation(summary = "카테고리로 시설 정보 가져오기", description = "카테고리로 데이터를 가져와서 파싱하여 보여줍니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 변환에 성공했습니다"),
            @ApiResponse(responseCode = "401", description = "API 요청 중 에러 발생"),
            @ApiResponse(responseCode = "404", description = "시설을 찾을 수 없습니다"),  // Facility not found
            @ApiResponse(responseCode = "409", description = "카테고리 형식이 맞지 않습니다") // Invalid Category
    })
    @GetMapping("/{facilityCategory}")
    public ResponseEntity<?> getFacility(@PathVariable String facilityCategory) {
        return ResponseEntity.ok(facilityAPIService.fetchSportFacilityData(facilityCategory));
    }

    @Operation(summary = "카테고리로 가져온 시설 정보 저장", description = "카테고리로 데이터를 가져온 후 파싱하여 데이터를 저장합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 저장 성공"),
            @ApiResponse(responseCode = "401", description = "API 요청 중 에러 발생"),
            @ApiResponse(responseCode = "409", description = "이미 있는 시설입니다") // Duplicate Facility
    })
    @PostMapping("/{facilityName}")
    public ResponseEntity<?> facilitySave(@PathVariable String facilityName) {
        String jsonRowData = facilityAPIService.fetchSportFacilityData(facilityName);
        Facility facility = facilityParsingService.parseFacility(jsonRowData);
        return ResponseEntity.ok(facilityService.addFacility(facility));
    }

    @Operation(summary = "카테고리 시설 전부 저장하기", description = "카테고리에 맞는 시설을 전부 파싱해 저장합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 저장 성공"),
            @ApiResponse(responseCode = "409", description = "카테고리 형식이 맞지 않습니다") // Invalid Category Format
    })
    @PostMapping("/{facilityName}/list")
    public ResponseEntity<?> facilitySaves(@PathVariable String facilityName) {
        String jsonRowData = facilityAPIService.fetchSportFacilityData(facilityName);
        List<Facility> facilities = facilityParsingService.parseFacilities(jsonRowData);
        return ResponseEntity.ok(facilityService.addFacilities(facilities));
    }

    @Operation(summary = "시설 내용을 업데이트 합니다", description = "시설의 내용이 변경되었을 경우 업데이트 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공"),
            @ApiResponse(responseCode = "401", description = "업데이트에 실패했습니다")  // Update failed
    })
    @PutMapping("/{facilityId}")
    public ResponseEntity<?> facilityUpdate(@PathVariable String facilityId, @RequestBody FacilityUpdateDTO facilityUpdateDTO) {
        return ResponseEntity.ok(facilityService.updateFacility(facilityUpdateDTO, facilityId));
    }

    @Operation(summary = "카테고리로 시설 목록 조회", description = "카테고리에 해당하는 모든 시설을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "시설 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 카테고리로 요청되었습니다")  // Invalid Category Format
    })

    @GetMapping("/{facilityCategory}/list")
    public ResponseEntity<?> facilityCategories(@PathVariable String facilityCategory) {
        return ResponseEntity.ok(facilitySearchService.getFacilitiesByCategory(FacilityCategory.fromName(facilityCategory)));
    }

    @Operation(summary = "시설 필터링 검색", description = "시설을 여러 조건으로 필터링하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "필터링된 시설 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 필터 설정입니다")  // Invalid filter
    })
    @GetMapping("/filter")
    public ResponseEntity<Page<FacilityResponseDTO>> facilityFilter(@RequestBody FacilityFilterDTO facilityFilterDTO) {
        return ResponseEntity.ok(facilitySearchService.getFacilitiesByFilter(facilityFilterDTO));
    }

    @Operation(summary = "모든 시설 데이터를 메모리에 저장", description = "모든 카테고리의 시설 데이터를 가져와 메모리에 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 변환 성공"),
            @ApiResponse(responseCode = "400", description = "시설 추가에 실패했습니다")  // Facility add failed
    })
    @PostMapping("/all-memory")
    public ResponseEntity<?> facilityAllMemory() {
        List<Facility> facilities = new ArrayList<>();
        for (FacilityCategory value : FacilityCategory.values()) {
            String facilityData = facilityAPIService.fetchSportFacilityData(value.getName());
            facilities.addAll(facilityParsingService.parseFacilities(facilityData));
        }
        facilityService.addFacilities(facilities);

        if (facilities.size() > 400) {
            return ResponseEntity.status(200).body(Map.of("데이터 변환:", "성공"));
        } else {
            return ResponseEntity.status(400).body(Map.of("데이터 변환:", "실패"));
        }
    }
    @GetMapping("/list")
    public ResponseEntity<Page<FacilityResponseDTO>> facilityList(){
        return ResponseEntity.ok(facilityService.getAllFacilities());
    }
}
