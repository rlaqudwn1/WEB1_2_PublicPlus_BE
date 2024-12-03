package backend.dev.activity.mapper;


import backend.dev.activity.dto.ActivityRequestDTO;
import backend.dev.activity.dto.ActivityResponseDTO;
import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;

public class MeetingActivityMapper {
    public static ActivityRequestDTO createDTOMapping(MeetingBoardRequestDTO board) {
        return ActivityRequestDTO.builder()
                .title(board.getMbTitle())
                .description(board.getMbContent())
                .startTime(board.getStartTime())
                .endTime(board.getEndTime())
                .location(board.getMbLocation())
                .maxParticipants(board.getMaxParticipants())
                .build();
    }
}
