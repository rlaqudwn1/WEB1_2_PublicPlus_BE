package backend.dev.facility.controller;

import backend.dev.facility.dto.ErrorResponseDTO;
import backend.dev.facility.dto.FacilityFilterDTO;
import backend.dev.facility.dto.FacilityPageResponseDTO;
import backend.dev.facility.dto.facility.FacilityLocationDTO;
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
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@Tag(name = "FacilityController", description = "시설 검색정보, 상세정보를 다루는 API입니다.")
public class FacilityDetailController {

    private final FacilityDetailService facilityDetailService;
    private final FacilitySearchService facilitySearchService;
    private final FacilityParsingService facilityParsingService;
    private final FacilityAPIService facilityAPIService;
    @Operation(summary = "시설 상세 정보 가져오기", description = "시설의 이름으로 상세 정보를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 데이터를 가져왔습니다."),
            @ApiResponse(responseCode = "404", description = "시설을 찾을 수 없습니다.")
    })
    @GetMapping("")
    public ResponseEntity<?> getFacilityDetail(@RequestParam String facilityName) {
        String data = facilityAPIService.fetchSportFacilityData(facilityName);
        return ResponseEntity.ok(data);
    }

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

    @Operation(summary = "시설 목록 페이지 조회", description = "모든 시설 목록을 페이지 단위로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "시설 목록 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityPageResponseDTO.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"content\": [\n" +
                                    "    {\n" +
                                    "      \"facilityId\": \"S210401100008601453\",\n" +
                                    "      \"facilityName\": \"마포 난지천 인조잔디축구장\",\n" +
                                    "      \"area\": \"마포구\",\n" +
                                    "      \"facilityCategory\": \"FOOTBALL_FIELD\",\n" +
                                    "      \"reservationStartDate\": \"2024-11-27T01:44:05.959Z\",\n" +
                                    "      \"reservationEndDate\": \"2024-11-27T01:44:05.959Z\"\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "      \"facilityId\": \"S210401100008601454\",\n" +
                                    "      \"facilityName\": \"강동구 실내 배드민턴장\",\n" +
                                    "      \"area\": \"강동구\",\n" +
                                    "      \"facilityCategory\": \"BADMINTON_COURT\",\n" +
                                    "      \"reservationStartDate\": \"2024-11-28T01:44:05.959Z\",\n" +
                                    "      \"reservationEndDate\": \"2024-11-28T01:44:05.959Z\"\n" +
                                    "    }\n" +
                                    "  ],\n" +
                                    "  \"pageable\": {\n" +
                                    "    \"pageNumber\": 0,\n" +
                                    "    \"pageSize\": 10,\n" +
                                    "    \"sort\": {\n" +
                                    "      \"sorted\": true,\n" +
                                    "      \"unsorted\": false,\n" +
                                    "      \"empty\": false\n" +
                                    "    }\n" +
                                    "  },\n" +
                                    "  \"totalElements\": 200,\n" +
                                    "  \"totalPages\": 20,\n" +
                                    "  \"last\": false,\n" +
                                    "  \"first\": true,\n" +
                                    "  \"size\": 10,\n" +
                                    "  \"number\": 0,\n" +
                                    "  \"sort\": {\n" +
                                    "    \"sorted\": true,\n" +
                                    "    \"unsorted\": false,\n" +
                                    "    \"empty\": false\n" +
                                    "  },\n" +
                                    "  \"numberOfElements\": 10\n" +
                                    "}"))
            )})
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
            return ResponseEntity.status(200).body(Map.of("데이터 수집", "완료"));
        }
        throw FacilityException.FACILITY_EXCEPTION.getFacilityTaskException();
    }

    @Operation(summary = "시설 필터링 검색", description = "시설 검색정보를 여러 조건으로 필터링하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "필터링된 시설 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "필터링 에러",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = "{\"message\":\"Invalid input data\"}")))
    })
    @PostMapping("/filter")
    public ResponseEntity<Page<FacilityResponseDTO>> facilityFilter(@RequestBody FacilityFilterDTO facilityFilterDTO) {
        Page<FacilityResponseDTO> filteredFacilities = facilitySearchService.getFacilitiesByFilter(facilityFilterDTO);
        return ResponseEntity.ok(filteredFacilities);
    }
    @Operation(summary = "위치기반 시설 검색",
            description = "주어진 위도, 경도, 반경에 해당하는 시설들을 검색합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "위치 정보와 반경을 포함한 검색 요청 데이터",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityLocationDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "성공적으로 시설 목록을 반환",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class),
                                    examples = @ExampleObject(value = "{\n" +
                                            "  \"totalElements\": 2,\n" +
                                            "  \"totalPages\": 1,\n" +
                                            "  \"size\": 5,\n" +
                                            "  \"content\": [\n" +
                                            "    {\n" +
                                            "      \"facilityId\": \"S241010135430829736\",\n" +
                                            "      \"facilityName\": \"서울대공원 리틀야구장\",\n" +
                                            "      \"facilityCategory\": \"BASEBALL_FIELD\",\n" +
                                            "      \"area\": \"과천시\",\n" +
                                            "      \"priceType\": false,\n" +
                                            "      \"facilityImage\": \"https://example.com/image1.jpg\",\n" +
                                            "      \"reservationStartDate\": \"2024-10-14T10:00:00\",\n" +
                                            "      \"reservationEndDate\": \"2024-10-16T16:00:00\"\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"facilityId\": \"S241010135430829737\",\n" +
                                            "      \"facilityName\": \"서울대공원 풋살장\",\n" +
                                            "      \"facilityCategory\": \"FOOTBALL_FIELD\",\n" +
                                            "      \"area\": \"과천시\",\n" +
                                            "      \"priceType\": true,\n" +
                                            "      \"facilityImage\": \"https://example.com/image2.jpg\",\n" +
                                            "      \"reservationStartDate\": \"2024-10-15T10:00:00\",\n" +
                                            "      \"reservationEndDate\": \"2024-10-17T16:00:00\"\n" +
                                            "    }\n" +
                                            "  ],\n" +
                                            "  \"number\": 0,\n" +
                                            "  \"sort\": [\n" +
                                            "    {\n" +
                                            "      \"direction\": \"ASC\",\n" +
                                            "      \"nullHandling\": \"NATIVE\",\n" +
                                            "      \"ascending\": true,\n" +
                                            "      \"property\": \"facilityName\",\n" +
                                            "      \"ignoreCase\": true\n" +
                                            "    }\n" +
                                            "  ],\n" +
                                            "  \"first\": true,\n" +
                                            "  \"last\": true,\n" +
                                            "  \"numberOfElements\": 2,\n" +
                                            "  \"pageable\": {\n" +
                                            "    \"offset\": 0,\n" +
                                            "    \"sort\": [\n" +
                                            "      {\n" +
                                            "        \"direction\": \"ASC\",\n" +
                                            "        \"nullHandling\": \"NATIVE\",\n" +
                                            "        \"ascending\": true,\n" +
                                            "        \"property\": \"facilityName\",\n" +
                                            "        \"ignoreCase\": true\n" +
                                            "      }\n" +
                                            "    ],\n" +
                                            "    \"paged\": true,\n" +
                                            "    \"pageSize\": 5,\n" +
                                            "    \"pageNumber\": 0,\n" +
                                            "    \"unpaged\": false\n" +
                                            "  },\n" +
                                            "  \"empty\": false\n" +
                                            "}")))
            })
    @PostMapping("/location")
    public ResponseEntity<Page<FacilityResponseDTO>> searchByLocation(@RequestBody FacilityLocationDTO facilityLocationDTO) {
        Page<FacilityResponseDTO> result = facilitySearchService.getFacilitiesNearBy(facilityLocationDTO);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "시설 상세 정보 업데이트", description = "시설 상세 정보를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 시설 상세 정보가 업데이트되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDetailsResponseDTO.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"facilityId\": \"S210401100008601453\",\n" +
                                    "  \"facilityName\": \"○ [평일] (주간) 마포 난지천 인조잔디축구장 (업데이트)\",\n" +
                                    "  \"facilityCategory\": \"FOOTBALL_FIELD\",\n" +
                                    "  \"area\": \"마포구\",\n" +
                                    "  \"priceType\": true,\n" +
                                    "  \"facilityImage\": \"https://example.com/image.jpg\",\n" +
                                    "  \"reservationStartDate\": \"2024-11-27T01:44:05.959Z\",\n" +
                                    "  \"reservationEndDate\": \"2024-11-27T01:44:05.959Z\",\n" +
                                    "  \"facilityNumber\": \"02-3153-9874\",\n" +
                                    "  \"reservationURL\": \"https://example.com/reserve\",\n" +
                                    "  \"facilityLocation\": \"서울특별시 산악문화체험센터>난지천인조잔디축구장\",\n" +
                                    "  \"facilityDescription\": \"1. 공공시설 예약서비스 이용시 필수 준수사항...\",\n" +
                                    "  \"serviceStartDate\": \"2024-01-01\",\n" +
                                    "  \"serviceEndDate\": \"2024-12-31\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "404", description = "시설을 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = "{\"message\":\"Facility not found\"}"))),
            @ApiResponse(responseCode = "400", description = "업데이트 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(value = "{\"message\":\"Invalid input data\"}")))
    })
    @PutMapping("/{facilityId}")
    public ResponseEntity<FacilityDetailsResponseDTO> updateFacilityDetail(@PathVariable String facilityId, @Validated @RequestBody FacilityDetailsUpdateDTO updateDetails) {
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
    public ResponseEntity<Map<String, String>> deleteFacilityDetail(@PathVariable String facilityId) {
        facilityDetailService.deleteFacilityDetail(facilityId);
        return ResponseEntity.ok(Map.of("message", "Facility deleted successfully"));
    }
    @DeleteMapping("/all")
    public ResponseEntity<Map<String, String>> deleteAll() {
        facilityDetailService.deleteAllFacilityDetails();
        return ResponseEntity.ok(Map.of("message", "Facility deleted successfully"));
    }
}