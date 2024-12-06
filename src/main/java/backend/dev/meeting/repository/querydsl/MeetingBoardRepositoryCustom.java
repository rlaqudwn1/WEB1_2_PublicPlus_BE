package backend.dev.meeting.repository.querydsl;

import backend.dev.meeting.dto.request.BoardFilterDTO;
import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import backend.dev.meeting.entity.MeetingBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingBoardRepositoryCustom {
    Page<MeetingBoard> findMeetingBoards(BoardFilterDTO boardFilterDTO, Pageable pageable);
}
