package backend.dev.facility.controller;

import backend.dev.facility.dto.facilitydetails.FacilityDetailsResponseDTO;
import backend.dev.facility.dto.facilitydetails.FacilityDetailsUpdateDTO;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.exception.FacilityException;
import backend.dev.facility.service.FacilityAPIService;
import backend.dev.facility.service.FacilityDetailService;
import backend.dev.facility.service.FacilityParsingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final FacilityParsingService facilityParsingService;
    private final FacilityAPIService facilityAPIService;

    @Operation(summary = "시설 상세 정보 가져오기", description = "시설의 이름으로 상세 정보를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 데이터를 가져왔습니다."),
            @ApiResponse(responseCode = "404", description = "시설을 찾을 수 없습니다.")
    })
    @GetMapping("/{facilityName}")
    public ResponseEntity<?> getFacilityDetail(@PathVariable String facilityName) {
        return ResponseEntity.ok(facilityAPIService.fetchSportFacilityData(facilityName));
    }

    @Operation(summary = "시설 상세 정보 읽기", description = "시설 ID로 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 시설 상세 정보를 조회했습니다."),
            @ApiResponse(responseCode = "404", description = "시설을 찾을 수 없습니다.")
    })
    @GetMapping("")
    public ResponseEntity<?> readFacilityDetails(@RequestParam String facilityId) {
        return ResponseEntity.ok(facilityDetailService.getFacilityDetails(facilityId));
    }

    @Operation(summary = "시설 상세 정보 목록 저장", description = "시설 이름으로 데이터를 가져와서 시설 상세 정보를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 시설 상세 정보 목록을 저장했습니다."),
            @ApiResponse(responseCode = "400", description = "데이터 형식 오류.")
    })
    @PostMapping("/{facilityName}/list")
    public ResponseEntity<?> addFacilityDetails(@PathVariable String facilityName) {
        String jsonRawData = facilityAPIService.fetchSportFacilityData(facilityName);
        List<FacilityDetails> facilityDetails = facilityParsingService.parseFacilityDetailsList(jsonRawData);
        List<FacilityDetailsResponseDTO> facilityDetailsResponseDTOS = facilityDetailService.addFacilityDetailsList(facilityDetails);
        return ResponseEntity.ok(facilityDetailsResponseDTOS);
    }

    @Operation(summary = "모든 시설 상세 정보 저장", description = "모든 카테고리의 시설 데이터를 가져와서 저장합니다.")
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
            @ApiResponse(responseCode = "200", description = "성공적으로 시설 상세 정보가 업데이트되었습니다."),
            @ApiResponse(responseCode = "404", description = "시설을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "400", description = "업데이트 실패")
    })

    @PutMapping("/{facilityId}")
    public ResponseEntity<?> updateFacilityDetail(@PathVariable String facilityId, @RequestBody FacilityDetailsUpdateDTO updateDetails) {
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
    public ResponseEntity<?> deleteFacilityDetail(@PathVariable String facilityId) {
        boolean deleted = facilityDetailService.deleteFacilityDetail(facilityId);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "시설 상세 정보가 성공적으로 삭제되었습니다."));
        }
        return ResponseEntity.status(400).body(Map.of("message", "시설 상세 정보 삭제 실패"));
    }
}
