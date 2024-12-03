package backend.dev.activity.service;

import backend.dev.activity.mapper.MeetingActivityMapper;
import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import backend.dev.meeting.service.MeetingBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityMeetingFacade {
    private final ActivityService activityService;
    private final MeetingBoardService meetingBoardService;

    public MeetingBoardResponseDTO createMeeting(MeetingBoardRequestDTO meetingBoardRequestDTO, String email) {
        MeetingBoardResponseDTO responseDTO = meetingBoardService.createMeetingBoard(meetingBoardRequestDTO,email);
        activityService.createActivity(MeetingActivityMapper.createDTOMapping(meetingBoardRequestDTO), email);
        return responseDTO;
    }
    public MeetingBoardResponseDTO updateMeeting(MeetingBoardRequestDTO meetingBoardRequestDTO, String email,Long activityId) {
        MeetingBoardResponseDTO meetingBoardResponseDTO = meetingBoardService.updateMeetingBoard(activityId, meetingBoardRequestDTO,email);
        activityService.updateActivity(MeetingActivityMapper.createDTOMapping(meetingBoardRequestDTO), activityId, email);
        return meetingBoardResponseDTO;
    }
}
