package backend.dev.meeting.service;

import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import backend.dev.meeting.entity.MeetingBoard;
import backend.dev.meeting.repository.MeetingBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingBoardService {

    private final MeetingBoardRepository meetingBoardRepository;

    // 모임 생성
    public MeetingBoardResponseDTO createMeetingBoard(MeetingBoardRequestDTO dto) {
        MeetingBoard meetingBoard = mapToEntity(dto);
        MeetingBoard saved = meetingBoardRepository.save(meetingBoard);
        return mapToResponseDTO(saved);
    }

    // 모임 전체 조회
    public List<MeetingBoardResponseDTO> getAllMeetingBoards() {
        return meetingBoardRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // 모임 조회
    public MeetingBoardResponseDTO getMeetingBoardById(Long id) {
        MeetingBoard meetingBoard = meetingBoardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 모임 게시판이 존재하지 않습니다."));
        return mapToResponseDTO(meetingBoard);
    }

    // 모임 수정
    public MeetingBoardResponseDTO updateMeetingBoard(Long id, MeetingBoardRequestDTO dto) {
        MeetingBoard meetingBoard = meetingBoardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 모임 게시판이 존재하지 않습니다."));

        meetingBoard.setSportType(dto.getSportType());
        meetingBoard.setMbTitle(dto.getMbTitle());
        meetingBoard.setMbContent(dto.getMbContent());
        meetingBoard.setMbDate(dto.getMbDate());
        meetingBoard.setMbTime(dto.getMbTime());
        meetingBoard.setMbLocation(dto.getMbLocation());
        meetingBoard.setMbHost(dto.getMbHost());
        meetingBoard.setMaxParticipants(dto.getMaxParticipants());

        MeetingBoard updated = meetingBoardRepository.save(meetingBoard);
        return mapToResponseDTO(updated);
    }

    // 모임 삭제
    public void deleteMeetingBoard(Long id) {
        if (!meetingBoardRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID의 모임 게시판이 존재하지 않습니다.");
        }
        meetingBoardRepository.deleteById(id);
    }

    // Helper Methods for Mapping
    private MeetingBoard mapToEntity(MeetingBoardRequestDTO dto) {
        MeetingBoard entity = new MeetingBoard();
        entity.setSportType(dto.getSportType());
        entity.setMbTitle(dto.getMbTitle());
        entity.setMbContent(dto.getMbContent());
        entity.setMbDate(dto.getMbDate());
        entity.setMbTime(dto.getMbTime());
        entity.setMbLocation(dto.getMbLocation());
        entity.setMbHost(dto.getMbHost());
        entity.setMaxParticipants(dto.getMaxParticipants());
        return entity;
    }

    private MeetingBoardResponseDTO mapToResponseDTO(MeetingBoard entity) {
        MeetingBoardResponseDTO dto = new MeetingBoardResponseDTO();
        dto.setMbId(entity.getMbId());
        dto.setSportType(entity.getSportType());
        dto.setMbTitle(entity.getMbTitle());
        dto.setMbContent(entity.getMbContent());
        dto.setMbDate(entity.getMbDate());
        dto.setMbTime(entity.getMbTime());
        dto.setMbLocation(entity.getMbLocation());
        dto.setMbHost(entity.getMbHost());
        dto.setMaxParticipants(entity.getMaxParticipants());
        dto.setMbCreatedDate(entity.getMbCreatedDate());
        dto.setMbLastModifiedDate(entity.getMbLastModifiedDate());
        return dto;
    }
}
