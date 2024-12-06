package backend.dev.meeting.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@Data
@Tag(name = "모임 게시판 서치를 위한 DTO")
public class BoardFilterDTO {
    @Schema(name = "타이틀")
    private String title;
    @Schema(name = "장소")
    private String location;
    @Schema(name = "스포츠 타입", description = "Enum에서 영어로 BADMINTON,SOCCER ... 으로 되어있습니다", example = "SOCCER")
    private String sportType;
}
