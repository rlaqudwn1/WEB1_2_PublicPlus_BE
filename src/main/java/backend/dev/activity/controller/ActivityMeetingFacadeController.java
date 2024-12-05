package backend.dev.activity.controller;


import backend.dev.activity.service.ActivityMeetingFacade;
import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/facade")
@Tag(name = "Activity-MeetingBoard Controller" ,description = "모임과 모임게시판을 연결시켜 관리하는 api 입니다")
public class ActivityMeetingFacadeController {
    private final ActivityMeetingFacade activityMeetingFacade;

    @Operation(summary = "모임 생성하기", description = "모임 게시판과 모임을 동시에 생성합니다")
    @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "생성 성공"),
          @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다")
    })
    @PostMapping
    public MeetingBoardResponseDTO createMeetingActivity(@RequestBody MeetingBoardRequestDTO dto){
        return ResponseEntity.ok(activityMeetingFacade.createMeeting(dto)).getBody();
    }
    @Operation(summary = "모임 수정하기", description = "모임 게시판과 모임을 동시에 수정합니다")
    @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "업데이트 성공"),
          @ApiResponse(responseCode = "404", description = "모임(게시판)을 찾을 수 없습니다")
    })
    @PutMapping
    public MeetingBoardResponseDTO updateMeetingActivity(@RequestBody MeetingBoardRequestDTO dto , @RequestParam Long activityId){
        return ResponseEntity.ok(activityMeetingFacade.updateMeeting(dto,activityId)).getBody();
    }
}
