package backend.dev.activity.controller;


import backend.dev.activity.service.ActivityMeetingFacade;
import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/facade")
public class ActivityMeetingFacadeController {
    private final ActivityMeetingFacade activityMeetingFacade;
    @PostMapping
    public MeetingBoardResponseDTO createMeetingActivity(@RequestBody MeetingBoardRequestDTO dto , @RequestParam String email){
        return ResponseEntity.ok(activityMeetingFacade.createMeeting(dto, email)).getBody();
    }
    @PutMapping
    public MeetingBoardResponseDTO updateMeetingActivity(@RequestBody MeetingBoardRequestDTO dto , @RequestParam Long activityId, @RequestParam String email){
        return ResponseEntity.ok(activityMeetingFacade.updateMeeting(dto,email,activityId)).getBody();
    }
}
