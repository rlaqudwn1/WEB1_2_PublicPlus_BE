package backend.dev.meeting.service;

import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import backend.dev.meeting.entity.MeetingBoard;
import backend.dev.meeting.entity.SportType;
import backend.dev.meeting.repository.MeetingBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeetingBoardServiceTest {

    @Mock
    private MeetingBoardRepository meetingBoardRepository;

    @InjectMocks
    private MeetingBoardService meetingBoardService;

    private MeetingBoardRequestDTO meetingBoardRequestDTO;
    private MeetingBoard meetingBoard;

    @BeforeEach
    void setUp() {
        // Mockito 초기화
        MockitoAnnotations.openMocks(this);  // Mock 객체 초기화

        // 테스트 데이터 준비
        meetingBoardRequestDTO = new MeetingBoardRequestDTO();
        meetingBoardRequestDTO.setSportType(SportType.valueOf("SOCCER"));
        meetingBoardRequestDTO.setMbTitle("Soccer Match");
        meetingBoardRequestDTO.setMbContent("Friendly match at the park.");
        meetingBoardRequestDTO.setMbDate(LocalDate.of(2024, 11, 21));
        meetingBoardRequestDTO.setMbTime(LocalTime.of(10, 30));
        meetingBoardRequestDTO.setMbLocation("City Park");
        meetingBoardRequestDTO.setMbHost("John Doe");
        meetingBoardRequestDTO.setMaxParticipants(10);

        meetingBoard = new MeetingBoard(meetingBoardRequestDTO);
        meetingBoard.setMbId(1L);
    }

    @Test
    void createMeetingBoard_ShouldReturnMeetingBoardResponseDTO() {
        // given
        // meetingBoard 객체가 올바르게 설정되어 있어야 함
        meetingBoard.setMbTitle("Soccer Match");
        when(meetingBoardRepository.save(any(MeetingBoard.class))).thenReturn(meetingBoard);

        // when
        MeetingBoardResponseDTO responseDTO = meetingBoardService.createMeetingBoard(meetingBoardRequestDTO);

        // then
        assertNotNull(responseDTO);  // responseDTO가 null이 아님을 확인
        assertEquals("Soccer Match", responseDTO.getMbTitle());  // 기대하는 값과 비교
        verify(meetingBoardRepository, times(1)).save(any(MeetingBoard.class));  // save가 정확히 한 번 호출되었는지 확인
    }

    @Test
    void getMeetingBoardById_ShouldReturnMeetingBoardResponseDTO() {
        // given
        // MeetingBoard 객체가 제대로 생성되어 있는지 확인
        meetingBoard.setMbTitle("Soccer Match");
        when(meetingBoardRepository.findById(1L)).thenReturn(Optional.of(meetingBoard));

        // when
        MeetingBoardResponseDTO responseDTO = meetingBoardService.getMeetingBoardById(1L);

        // then
        assertNotNull(responseDTO);  // null이 아닌지 확인
        assertEquals("Soccer Match", responseDTO.getMbTitle());  // 기대하는 값과 비교
        verify(meetingBoardRepository, times(1)).findById(1L);  // repository 호출 확인
    }

    @Test
    void getMeetingBoardById_ShouldThrowException_WhenNotFound() {
        // given
        when(meetingBoardRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> meetingBoardService.getMeetingBoardById(1L));
    }

    @Test
    void updateMeetingBoard_ShouldReturnUpdatedMeetingBoardResponseDTO() {
        // given
        meetingBoardRequestDTO.setMbTitle("Updated Soccer Match");
        when(meetingBoardRepository.findById(1L)).thenReturn(Optional.of(meetingBoard));
        when(meetingBoardRepository.save(any(MeetingBoard.class))).thenReturn(meetingBoard);

        // when
        MeetingBoardResponseDTO updatedResponseDTO = meetingBoardService.updateMeetingBoard(1L, meetingBoardRequestDTO);

        // then
        assertEquals("Updated Soccer Match", updatedResponseDTO.getMbTitle());
        verify(meetingBoardRepository, times(1)).save(any(MeetingBoard.class));
    }

    @Test
    void deleteMeetingBoard_ShouldDeleteMeetingBoard() {
        // given
        when(meetingBoardRepository.existsById(1L)).thenReturn(true);

        // when
        meetingBoardService.deleteMeetingBoard(1L);

        // then
        verify(meetingBoardRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMeetingBoard_ShouldThrowException_WhenNotFound() {
        // given
        when(meetingBoardRepository.existsById(1L)).thenReturn(false);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> meetingBoardService.deleteMeetingBoard(1L));
    }
}
