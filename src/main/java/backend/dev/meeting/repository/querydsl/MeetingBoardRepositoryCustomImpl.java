package backend.dev.meeting.repository.querydsl;

import backend.dev.meeting.dto.request.BoardFilterDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import backend.dev.meeting.entity.MeetingBoard;
import backend.dev.meeting.entity.QMeetingBoard;
import backend.dev.meeting.entity.SportType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MeetingBoardRepositoryCustomImpl implements MeetingBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MeetingBoard> findMeetingBoards(BoardFilterDTO boardFilterDTO, Pageable pageable) {
        QMeetingBoard qMeetingBoard = QMeetingBoard.meetingBoard;

        BooleanBuilder builder = new BooleanBuilder();

        if (boardFilterDTO.getTitle() != null){
            builder.and(qMeetingBoard.mbTitle.containsIgnoreCase(boardFilterDTO.getTitle()));
        }
        if (boardFilterDTO.getLocation() != null){
            builder.and(qMeetingBoard.mbLocation.containsIgnoreCase(boardFilterDTO.getLocation()));
        }
        if (boardFilterDTO.getSportType() != null){
            builder.and(qMeetingBoard.sportType.eq(SportType.valueOf(boardFilterDTO.getSportType())));
        }
        List<MeetingBoard> meetingBoardList = queryFactory.selectFrom(qMeetingBoard)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(meetingBoardList, pageable, meetingBoardList.size());
    }
}
