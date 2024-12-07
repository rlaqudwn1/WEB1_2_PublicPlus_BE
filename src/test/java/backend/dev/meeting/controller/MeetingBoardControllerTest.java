package backend.dev.meeting.controller;

import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import backend.dev.meeting.entity.SportType;
import backend.dev.meeting.service.MeetingBoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MeetingBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MeetingBoardService meetingBoardService;

    private MeetingBoardRequestDTO meetingBoardRequestDTO;

    @BeforeEach
    void setUp() {
        // SecurityContext Mocking
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("user123");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // 테스트용 DTO 초기화
        meetingBoardRequestDTO = new MeetingBoardRequestDTO();
        meetingBoardRequestDTO.setSportType(SportType.SOCCER);
        meetingBoardRequestDTO.setMbTitle("Test Meeting");
        meetingBoardRequestDTO.setMbContent("Test Content");
        meetingBoardRequestDTO.setStartTime(LocalDateTime.of(2024, 12, 1, 15, 0));
        meetingBoardRequestDTO.setEndTime(LocalDateTime.of(2024, 12, 1, 18, 0));
        meetingBoardRequestDTO.setMbLocation("Test Location");
        meetingBoardRequestDTO.setMaxParticipants(10);
        meetingBoardRequestDTO.setOpenChatLink("https://open.kakao.com/o/example"); // 필수 필드 추가
    }

    @Test
    void testCreateMeeting() throws Exception {
        MeetingBoardResponseDTO mockResponse = new MeetingBoardResponseDTO(
                1L, SportType.SOCCER, "Test Meeting", "Test Content",
                LocalDateTime.of(2024, 12, 1, 15, 0),
                LocalDateTime.of(2024, 12, 1, 18, 0),
                "Test Location", 10,
                LocalDateTime.now(), LocalDateTime.now(),
                "user123",
                "http://openChatLink.com/example"
        );

        when(meetingBoardService.createMeetingBoard(any(MeetingBoardRequestDTO.class), any(String.class)))
                .thenReturn(mockResponse);

        String requestJson = objectMapper.writeValueAsString(meetingBoardRequestDTO);

        mockMvc.perform(post("/api/meetingboard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mbTitle").value("Test Meeting"))
                .andExpect(jsonPath("$.mbHostId").value("user123"));
    }
}