package backend.dev.meeting.repository;

import backend.dev.meeting.entity.MeetingBoard;
import backend.dev.meeting.repository.querydsl.MeetingBoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingBoardRepository extends JpaRepository<MeetingBoard, Long>, MeetingBoardRepositoryCustom {

}
