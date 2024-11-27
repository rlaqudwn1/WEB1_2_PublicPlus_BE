package backend.dev.facility.controller;

import backend.dev.facility.dto.FacilityFilterDTO;
import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.dto.facilitydetails.FacilityDetailsResponseDTO;
import backend.dev.facility.dto.facilitydetails.FacilityDetailsUpdateDTO;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.exception.FacilityException;
import backend.dev.facility.service.FacilityAPIService;
import backend.dev.facility.service.FacilityDetailService;
import backend.dev.facility.service.FacilitySearchService;
import backend.dev.facility.service.FacilityParsingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/facility-detail")
@Tag(name = "시설 상세 정보를 관리하는 API")
public class FacilityDetailController {

    private final FacilityDetailService facilityDetailService;
    private final FacilitySearchService facilitySearchService;
    private final FacilityParsingService facilityParsingService;
    private final FacilityAPIService facilityAPIService;

//    @Operation(summary = "시설 상세 정보 가져오기", description = "시설의 이름으로 상세 정보를 가져옵니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공적으로 데이터를 가져왔습니다."),
//            @ApiResponse(responseCode = "404", description = "시설을 찾을 수 없습니다.")
//    })
//    @GetMapping("/{facilityName}")
//    public ResponseEntity<?> getFacilityDetail(@PathVariable String facilityName) {
//        String data = facilityAPIService.fetchSportFacilityData(facilityName);
//        return ResponseEntity.ok(data);
//    }

    @Operation(summary = "시설 상세 정보 페이지 조회", description = "시설 ID로 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 시설 상세 정보를 조회했습니다."),
            @ApiResponse(responseCode = "404", description = "시설을 찾을 수 없습니다.")
    })
    @GetMapping("/{facilityId}")
    public ResponseEntity<FacilityDetailsResponseDTO> readFacilityDetails(@PathVariable String facilityId) {
        FacilityDetailsResponseDTO response = facilityDetailService.getFacilityDetails(facilityId);
        return ResponseEntity.ok(response);
    }

//    @Operation(summary = "시설 상세 정보 목록 저장", description = "시설 이름으로 데이터를 가져와서 시설 상세 정보를 저장합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공적으로 시설 상세 정보 목록을 저장했습니다."),
//            @ApiResponse(responseCode = "400", description = "데이터 형식 오류.")
//    })
//    @PostMapping("/{facilityName}/list")
//    public ResponseEntity<?> addFacilityDetails(@PathVariable String facilityName) {
//        String jsonRawData = facilityAPIService.fetchSportFacilityData(facilityName);
//        List<FacilityDetailsResponseDTO> facilityDetailsResponseDTOS = facilityDetailService.addFacilityDetailsList(
//                facilityParsingService.parseFacilityDetailsList(jsonRawData)
//        );
//        return ResponseEntity.ok(facilityDetailsResponseDTOS);
//    }

    @Operation(summary = "모든 시설 상세 정보 저장", description = "서울시 공공 서비스예약의 모든 시설 데이터를 API를 통해 가져와서 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 모든 시설 상세 정보를 저장했습니다."),
            @ApiResponse(responseCode = "400", description = "데이터 수집에 실패했습니다.")
    })
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

    @Operation(summary = "시설 상세 정보 업데이트", description = "시설 상세 정보를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 시설 상세 정보가 업데이트되었습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FacilityDetailsResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "시설을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "400", description = "업데이트 실패")
    })
    @PutMapping("/{facilityId}")
    public ResponseEntity<FacilityDetailsResponseDTO> updateFacilityDetail(@PathVariable String facilityId,@Validated @RequestBody FacilityDetailsUpdateDTO updateDetails) {
        FacilityDetailsResponseDTO facilityDetailsResponseDTO = facilityDetailService.updateFacilityDetails(updateDetails, facilityId);
        return ResponseEntity.ok(facilityDetailsResponseDTO);
    }

    @Operation(summary = "시설 상세 정보 삭제", description = "시설 상세 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 시설 상세 정보가 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "시설을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "400", description = "삭제 실패")
    })
    @DeleteMapping("/{facilityId}")
    public ResponseEntity<Map<String,String>> deleteFacilityDetail(@PathVariable String facilityId) {
        boolean deleted = facilityDetailService.deleteFacilityDetail(facilityId);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "시설 상세 정보가 성공적으로 삭제되었습니다."));
        }
        return ResponseEntity.status(400).body(Map.of("message", "시설 상세 정보 삭제 실패"));
    }

    @Operation(summary = "모든 시설 정보 삭제", description = "모든 시설 상세 정보를 삭제합니다.")
    @DeleteMapping("/all")
    public ResponseEntity<Map<String,String>> deleteAllFacilityDetails() {
        facilityDetailService.deleteAllFacilityDetails();
        return ResponseEntity.ok(Map.of("message", "모든 시설 정보가 삭제되었습니다."));
    }

    @Operation(summary = "시설 필터링 검색", description = "시설을 여러 조건으로 필터링하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "필터링된 시설 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 필터 설정입니다")
    })
    @PostMapping("/filter")
    public ResponseEntity<Page<FacilityResponseDTO>> facilityFilter(@RequestBody FacilityFilterDTO facilityFilterDTO) {
        Page<FacilityResponseDTO> filteredFacilities = facilitySearchService.getFacilitiesByFilter(facilityFilterDTO);
        return ResponseEntity.ok(filteredFacilities);
    }

    @Operation(summary = "카테고리별 시설 목록 조회", description = "시설 카테고리로 데이터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 카테고리별 시설 목록을 조회했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 카테고리 형식입니다.")
    })
    @GetMapping("/category/{facilityCategory}")
    public ResponseEntity<Page<FacilityResponseDTO>> getFacilitiesByCategory(@PathVariable String facilityCategory) {
        FacilityCategory category = FacilityCategory.fromName(facilityCategory);
        Page<FacilityResponseDTO> facilities = facilitySearchService.getFacilitiesByCategory(category);
        return ResponseEntity.ok(facilities);
    }

    @Operation(summary = "시설 목록 조회", description = "모든 시설 목록을 페이지 단위로 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<Page<FacilityResponseDTO>> getAllFacilities() {
        Page<FacilityResponseDTO> allFacilities = facilityDetailService.getAllFacility();
        return ResponseEntity.ok(allFacilities);
    }

    @Operation(summary = "시설 상세 정보 목록 조회", description = "모든 시설 상세 정보를 페이지 단위로 조회합니다.")
    @GetMapping("/details")
    public ResponseEntity<Page<FacilityDetailsResponseDTO>> getAllFacilityDetails() {
        Page<FacilityDetailsResponseDTO> allDetails = facilityDetailService.getAllDetails();
        return ResponseEntity.ok(allDetails);
    }
}
