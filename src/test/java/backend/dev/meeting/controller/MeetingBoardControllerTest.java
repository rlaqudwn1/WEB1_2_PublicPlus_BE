package backend.dev.meeting.controller;

import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import backend.dev.meeting.entity.SportType;
import backend.dev.meeting.service.MeetingBoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MeetingBoardControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MeetingBoardService meetingBoardService;

    @Autowired
    private ObjectMapper objectMapper; // JSON 직렬화/역직렬화용

    @Test
    void testCreateMeeting() {
        // given: 요청 데이터 생성
        MeetingBoardRequestDTO requestDTO = new MeetingBoardRequestDTO();
        requestDTO.setMbTitle("Test Meeting");
        requestDTO.setMbContent("This is a test meeting.");
        requestDTO.setSportType(SportType.SOCCER);
        requestDTO.setMbDate(LocalDate.of(2024,12,01));
        requestDTO.setMbTime(LocalTime.of(10, 30));
        requestDTO.setMbLocation("Seoul");
        requestDTO.setMaxParticipants(10);

        String url = "http://localhost:" + port + "/api/v1/meetings";

        RestTemplate restTemplate = new RestTemplate();

        // when: POST 요청 보내기
        ResponseEntity<MeetingBoardResponseDTO> response = restTemplate.postForEntity(url, requestDTO, MeetingBoardResponseDTO.class);

        // then: 응답 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        MeetingBoardResponseDTO responseDTO = response.getBody();
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getMbTitle()).isEqualTo("Test Meeting");
        assertThat(responseDTO.getMbContent()).isEqualTo("This is a test meeting.");
    }
}
