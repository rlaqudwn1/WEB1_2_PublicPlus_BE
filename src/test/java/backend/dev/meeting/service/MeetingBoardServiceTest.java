package backend.dev.meeting.service;

import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import backend.dev.meeting.entity.MeetingBoard;
import backend.dev.meeting.entity.SportType;
import backend.dev.meeting.exception.UnauthorizedAccessException;
import backend.dev.meeting.repository.MeetingBoardRepository;
import backend.dev.user.entity.Role;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class MeetingBoardServiceTest {

    @Autowired
    private MeetingBoardService meetingBoardService;

    @MockBean
    private MeetingBoardRepository meetingBoardRepository;

    @MockBean
    private UserRepository userRepository;

    private MeetingBoardRequestDTO meetingBoardRequestDTO;

    private User adminUser;

    @BeforeEach
    void setUp() {
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        // 사용자 ID를 SecurityContextHolder에 설정
        Mockito.when(authentication.getName()).thenReturn("user123"); // 인증된 사용자 ID
        Mockito.when(authentication.isAuthenticated()).thenReturn(true); // 인증된 상태
        Mockito.when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context); // SecurityContextHolder에 컨텍스트 설정

        // Mock UserRepository
        adminUser = User.builder()
                .userId("user123")
                .role(Role.ADMIN)
                .build();
        when(userRepository.findById("user123")).thenReturn(Optional.of(adminUser));

        // 요청 데이터 초기화
        meetingBoardRequestDTO = new MeetingBoardRequestDTO();
        meetingBoardRequestDTO.setMbTitle("Test Meeting");
        meetingBoardRequestDTO.setMbContent("Test Content");
        meetingBoardRequestDTO.setSportType(SportType.SOCCER);
        meetingBoardRequestDTO.setStartTime(LocalDateTime.of(2024, 12, 1, 10, 30));
        meetingBoardRequestDTO.setEndTime(LocalDateTime.of(2024, 12, 1, 12, 30));
        meetingBoardRequestDTO.setMbLocation("Test Location");
        meetingBoardRequestDTO.setMaxParticipants(10);
    }

    @Test
    void createMeetingBoard_ShouldThrowException_WhenUserIsNotAdmin() {
        // Mock 일반 사용자
        when(userRepository.findById("user123")).thenReturn(Optional.of(
                User.builder()
                        .userId("user123")
                        .role(Role.USER) // 일반 사용자
                        .build()
        ));

        // Mock MeetingBoardRepository.save() to return null
        when(meetingBoardRepository.save(any(MeetingBoard.class))).thenAnswer(invocation -> {
            throw new UnauthorizedAccessException("인증되지 않은 사용자입니다.");
        });

        // Act & Assert
        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () ->
                meetingBoardService.createMeetingBoard(meetingBoardRequestDTO, "user123")
        );

        assertEquals("인증되지 않은 사용자입니다.", exception.getMessage());
    }

    @Test
    void createMeetingBoard_ShouldSucceed_WhenUserIsAdmin() {
        MeetingBoard mockMeetingBoard = MeetingBoard.builder()
                .mbId(1L)
                .mbTitle("Test Meeting")
                .mbContent("Test Content")
                .sportType(SportType.SOCCER)
                .mbHost(adminUser) // 테스트용 사용자
                .startTime(meetingBoardRequestDTO.getStartTime())
                .endTime(meetingBoardRequestDTO.getEndTime())
                .mbLocation(meetingBoardRequestDTO.getMbLocation())
                .maxParticipants(meetingBoardRequestDTO.getMaxParticipants())
                .build();

        when(meetingBoardRepository.save(any(MeetingBoard.class))).thenReturn(mockMeetingBoard);

        // Act
        MeetingBoardResponseDTO response = meetingBoardService.createMeetingBoard(meetingBoardRequestDTO, "user123"); // requesterId 추가

        // Assert
        assertNotNull(response);
        assertEquals("Test Meeting", response.getMbTitle());
        assertEquals("user123", response.getMbHostId());
    }
}