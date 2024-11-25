package backend.dev.meeting.controller;

import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import backend.dev.meeting.service.MeetingBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetingboard")
@RequiredArgsConstructor
public class MeetingBoardController {

    private final MeetingBoardService meetingBoardService;

    // 모임 생성
    @PostMapping
    public ResponseEntity<?> createMeetingBoard(@RequestBody MeetingBoardRequestDTO dto) {
        try {
            MeetingBoardResponseDTO response = meetingBoardService.createMeetingBoard(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }

    // 모임 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllMeetingBoards() {
        try {
            List<MeetingBoardResponseDTO> response = meetingBoardService.getAllMeetingBoards();
            if (response.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("모임이 없습니다.");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }

    // 모임 조회
    @GetMapping("/{mbId}")
    public ResponseEntity<?> getMeetingBoardById(@PathVariable("mbId") Long mbId) {
        try {
            MeetingBoardResponseDTO response = meetingBoardService.getMeetingBoardById(mbId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 모임을 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }

    // 모임 수정
    @PutMapping("/{mbId}")
    public ResponseEntity<?> updateMeetingBoard(@PathVariable("mbId") Long mbId, @RequestBody MeetingBoardRequestDTO dto) {
        try {
            MeetingBoardResponseDTO response = meetingBoardService.updateMeetingBoard(mbId, dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }

    // 모임 삭제
    @DeleteMapping("/{mbId}")
    public ResponseEntity<?> deleteMeetingBoard(@PathVariable("mbId") Long mbId) {
        try {
            meetingBoardService.deleteMeetingBoard(mbId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }
}
