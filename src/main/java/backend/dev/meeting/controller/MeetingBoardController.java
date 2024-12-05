package backend.dev.meeting.controller;

import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.meeting.dto.response.MeetingBoardResponseDTO;
import backend.dev.meeting.exception.MeetingBoardNotFoundException;
import backend.dev.meeting.service.MeetingBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetingboard")
@RequiredArgsConstructor
@Tag(name = "Meeting Board", description = "모임 게시판 관련 API")
public class MeetingBoardController {

    private final MeetingBoardService meetingBoardService;

    @Operation(summary = "모임 생성", description = "새로운 모임을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "모임 생성 성공",
                    content = @Content(schema = @Schema(implementation = MeetingBoardResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.") // 수정된 부분
    })
    @PostMapping
    public ResponseEntity<MeetingBoardResponseDTO> createMeetingBoard(
            @Valid @RequestBody MeetingBoardRequestDTO requestDTO) {

        // 인증된 사용자 ID 추출
        String requesterId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Service 호출 시 인증된 사용자 ID 전달
        MeetingBoardResponseDTO responseDTO = meetingBoardService.createMeetingBoard(requestDTO, requesterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(summary = "모임 전체 조회", description = "등록된 모든 모임을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MeetingBoardResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "모임이 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping
    public ResponseEntity<List<MeetingBoardResponseDTO>> getAllMeetingBoards() {
        List<MeetingBoardResponseDTO> meetings = meetingBoardService.getAllMeetingBoards();
        return ResponseEntity.ok(meetings); // 빈 목록이라도 200 반환
    }


    @Operation(summary = "모임 상세 조회", description = "특정 ID의 모임을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = MeetingBoardResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "모임을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{mbId}")
    public ResponseEntity<MeetingBoardResponseDTO> getMeetingBoardById(@PathVariable Long mbId) {
        try {
            MeetingBoardResponseDTO response = meetingBoardService.getMeetingBoardById(mbId);
            return ResponseEntity.ok(response);
        } catch (MeetingBoardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 처리
        }
    }

    @Operation(summary = "모임 수정", description = "특정 ID의 모임 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = MeetingBoardResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터"),
            @ApiResponse(responseCode = "403", description = "수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "모임을 찾을 수 없음")
    })
    @PutMapping("/{mbId}")
    public ResponseEntity<MeetingBoardResponseDTO> updateMeetingBoard(
            @PathVariable Long mbId, @Valid @RequestBody MeetingBoardRequestDTO dto) {

        // 인증된 사용자 ID 추출
        String requesterId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Service 호출
        MeetingBoardResponseDTO responseDTO = meetingBoardService.updateMeetingBoard(mbId, dto, requesterId);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "모임 삭제", description = "특정 ID의 모임 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
            @ApiResponse(responseCode = "404", description = "모임을 찾을 수 없음")
    })
    @DeleteMapping("/{mbId}")
    public ResponseEntity<Void> deleteMeetingBoard(@PathVariable Long mbId) {

        // 인증된 사용자 ID 추출
        String requesterId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Service 호출
        meetingBoardService.deleteMeetingBoard(mbId, requesterId);
        return ResponseEntity.noContent().build();
    }
}