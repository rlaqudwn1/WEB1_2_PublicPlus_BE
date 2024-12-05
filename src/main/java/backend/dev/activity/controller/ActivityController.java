package backend.dev.activity.controller;

import backend.dev.activity.dto.ActivityRequestDTO;
import backend.dev.activity.dto.ActivityResponseDTO;

import backend.dev.activity.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/activity")
@Tag(name = "ActivityController", description = "About googleCalendar And Activity API")
public class ActivityController {

    private final ActivityService activityService;

    @Operation(summary = "모임을 ID로 검색", description = "특정 ID에 해당하는 모임 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 모임이 존재하지 않음")
    })
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponseDTO> getActivity(@PathVariable Long activityId) {
        return ResponseEntity.ok(activityService.readActivity(activityId));
    }

    @Operation(summary = "모임 생성", description = "새로운 모임을 생성하고, 생성된 모임 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임 생성 성공"),
            @ApiResponse(responseCode = "400", description = "생성 데이터가 유효하지 않음")
    })
    @PostMapping
    public ResponseEntity<ActivityResponseDTO> createActivity(@RequestBody ActivityRequestDTO dto) {
        log.info(dto.toString());
        return ResponseEntity.status(201).body(activityService.createActivity(dto));
    }

    @Operation(summary = "모임 수정", description = "모임 정보를 업데이트하고, 수정된 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임 수정 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 모임이 존재하지 않음"),
            @ApiResponse(responseCode = "400", description = "요청 데이터가 유효하지 않음")
    })
    @PutMapping("/{activityId}")
    public ResponseEntity<ActivityResponseDTO> updateActivity(
            @RequestBody ActivityRequestDTO dto,
            @PathVariable Long activityId) {
        log.info(dto.toString());
        return ResponseEntity.ok(activityService.updateActivity(dto,activityId));
    }

    @Operation(summary = "모임 삭제", description = "특정 ID의 모임을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 모임이 존재하지 않음")
    })
    @DeleteMapping("/{activityId}")
    public ResponseEntity<Map<String, String>> deleteActivity(@PathVariable Long activityId) {
        activityService.deleteActivity(activityId);
        return ResponseEntity.status(200).body(Map.of("200", "deleted"));
    }

    @Operation(summary = "내 모임 조회", description = "유저 아이디를 통해 유저의 모임들을 조회합니다")
    @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "조회성공"),
          @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다"),
    })
    @GetMapping
    public ResponseEntity<Page<ActivityResponseDTO>> findActivitiesByUserId(@PageableDefault(size = 5,page = 0) Pageable pageable){
          return ResponseEntity.ok(activityService.findByUserId(pageable));
    }


//    @Operation(summary = "사용자의 모임 검색", description = "특정 사용자 이메일에 해당하는 모임 목록을 페이지 형태로 반환합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "검색 성공"),
//            @ApiResponse(responseCode = "400", description = "요청 파라미터가 유효하지 않음")
//    })
//    @GetMapping
//    public ResponseEntity<Page<ActivityResponseDTO>> getActivitesByUser(@RequestParam String userEmail) {
//        return ResponseEntity.ok(activityService.readActivitiesByUserEmail(userEmail));
//    }
}
