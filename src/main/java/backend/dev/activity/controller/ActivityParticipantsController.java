package backend.dev.activity.controller;


import backend.dev.activity.dto.ActivityResponseDTO;
import backend.dev.activity.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity-participants")
@RequiredArgsConstructor
@Tag(name = "Activity ParticipantsController " ,description = "모임 참가자에 대한 컨트롤러 입니다")
public class ActivityParticipantsController {
    private final ActivityService activityService;

    @Operation(summary = "모임 참가", description = "모임 아이디를 기반으로 모임을 참가합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참가 성공"),
            @ApiResponse(responseCode = "400", description = "모임에 더 참가할 수 없습니다")
    })
    @PostMapping("/{activityId}")
    public ResponseEntity<ActivityResponseDTO> joinActivity(@PathVariable Long activityId) {
        return ResponseEntity.ok(activityService.JoinActivity(activityId));
    }

    @Operation(summary = "모임 나가기", description = "모임 아이디 기반 모임 나가기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "모임을 찾을 수 없습니다")
    })
    @DeleteMapping("/{activityId}")
    public String quitActivity(@PathVariable Long activityId) {
        activityService.activityQuit(activityId);
        return "OK";
    }
}
