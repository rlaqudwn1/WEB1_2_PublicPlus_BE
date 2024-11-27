package backend.dev.facility.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDTO {
    @Schema(description = "에러 코드")
    private String errorCode;

    @Schema(description = "에러 메시지")
    private String message;

    @Schema(description = "에러에 대한 세부 설명")
    private String details;
}
