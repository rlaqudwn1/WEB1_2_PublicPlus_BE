package backend.dev.likes.controller;


import backend.dev.likes.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/like-facility")
@Tag(name = "facility-Like Controller", description = "시설의 좋아요를 관리하는 컨트롤러입니다")
public class FacilityLikeController {
    private final LikeService likeService;

    @Operation(summary = "좋아요", description = "시설 아이디를 받아 좋아요를 누릅니다")
    @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "좋아요"),
          @ApiResponse(responseCode = "400", description = "이미 좋아요를 눌렀습니다")
    })
    @PostMapping
    public String addlike(@RequestParam String facilityId){
        likeService.addLike(facilityId);
        return "좋아요";
    }
    @Operation(summary = "좋아요 취소", description = "좋아요를 누른 상태라면 좋아요 취소버튼 활성화 또는 버튼을 다시 누르면 좋아요 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 취소"),
            @ApiResponse(responseCode = "404", description = "유저(시설)을 찾을 수 없습니다")
    })
    @DeleteMapping
    public String dislike(@RequestParam String facilityId){
        likeService.removeLike(facilityId);
        return "좋아요 취소";
    }

}
