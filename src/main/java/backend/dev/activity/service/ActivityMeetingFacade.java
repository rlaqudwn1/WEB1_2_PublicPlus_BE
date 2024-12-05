package backend.dev.activity.service;

import backend.dev.activity.mapper.MeetingActivityMapper;
import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import backend.dev.meeting.service.MeetingBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityMeetingFacade {
    private final ActivityService activityService;
    private final MeetingBoardService meetingBoardService;

    public MeetingBoardResponseDTO createMeeting(MeetingBoardRequestDTO meetingBoardRequestDTO) {
        String requesterId = SecurityContextHolder.getContext().getAuthentication().getName();
        MeetingBoardResponseDTO responseDTO = meetingBoardService.createMeetingBoard(meetingBoardRequestDTO,requesterId);
        activityService.createActivity(MeetingActivityMapper.createDTOMapping(meetingBoardRequestDTO));
        return responseDTO;
    }
    public MeetingBoardResponseDTO updateMeeting(MeetingBoardRequestDTO meetingBoardRequestDTO,Long activityId) {
        String requesterId = SecurityContextHolder.getContext().getAuthentication().getName();
        MeetingBoardResponseDTO meetingBoardResponseDTO = meetingBoardService.updateMeetingBoard(activityId, meetingBoardRequestDTO,requesterId);
        activityService.updateActivity(MeetingActivityMapper.createDTOMapping(meetingBoardRequestDTO), activityId);
        return meetingBoardResponseDTO;
    }
}
