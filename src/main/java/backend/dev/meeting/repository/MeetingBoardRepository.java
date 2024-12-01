package backend.dev.meeting.repository;

import backend.dev.meeting.entity.MeetingBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingBoardRepository extends JpaRepository<MeetingBoard, Long> {

}
