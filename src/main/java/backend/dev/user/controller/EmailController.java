package backend.dev.user.controller;

import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.ErrorResponse;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Tag(name = "EmailController", description = "이메일 인증메일을 보내거나 이메일에 대한 확인 검증을 합니다")
public class EmailController {
    private final EmailService emailService;

    @Operation(summary = "이메일 발송", description = "이메일 인증을 위한 코드를 발송합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  \"httpStatus\": \"OK\",\n  \"message\": \"발송 완료\"\n}"))),
            @ApiResponse(responseCode = "400", description = "이미 인증을 보낸 메일일 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\n  \"httpStatus\": \"BAD_REQUEST\",\n  \"message\": \"이미 인증을 요청한 메일입니다\"\n}"))),
            @ApiResponse(responseCode = "500", description = "서버에서 메일 전송이 실패할 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\n  \"httpStatus\": \"INTERVAL_SERVER_ERROR\",\n  \"message\": \"메일 전송에 실패했습니다.\"\n}")))
    })
    @PostMapping
    public ResponseEntity<?> sendCode(
            @Schema(description = "인증번호를 보낼 email 주소", example = "example@example.com")
            @RequestParam("email")
            String email) {
        emailService.sendCodeToEmail(email);
        return ResponseEntity.ok().body(Map.of("httpStatus", HttpStatus.OK, "message", "발송완료"));
    }

    @Operation(summary = "검증", description = "보낸 코드값이 서버의 저장값과 일치하는지 확인합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  \"httpStatus\": \"OK\",\n  \"message\": \"인증 성공\"\n}"))),
            @ApiResponse(responseCode = "400", description = "인증 실패 : 인증번호 불일치", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  \"httpStatus\": \"BAD_REQUEST\",\n  \"message\": \"인증번호가 일치하지 않습니다\"\n}"))),
            @ApiResponse(responseCode = "400", description = "인증 실패 : 인증시간 초과", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  \"httpStatus\": \"BAD_REQUEST\",\n  \"message\": \"메일 인증 시간이 초과됐습니다\"\n}")))
    })
    @GetMapping
    public ResponseEntity<?> verifyCode(
            @Schema(description = "인증번호를 보낸 email 주소", example = "example@example.com")
            @RequestParam("email")
            String email,
            @Schema(description = "받은 인증번호", example = "123456")
            @RequestParam("code")
            String code) {
        if (emailService.verifyCode(email, code)) {
            return ResponseEntity.ok().body(Map.of("httpStatus", HttpStatus.OK, "message", "인증 성공"));
        } else {
            throw new PublicPlusCustomException(ErrorCode.NOT_MATCH_CERTIFICATION);
        }
    }

}
