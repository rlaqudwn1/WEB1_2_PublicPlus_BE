package backend.dev.user.controller;

import backend.dev.user.domain.DTO.UserDTO;
import backend.dev.user.domain.DTO.UserJoinDTO;
import backend.dev.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserDTO> join(@RequestBody UserJoinDTO userJoinDTO) {
        UserDTO join = userService.join(userJoinDTO);
        return ResponseEntity.ok(join);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> findMyInformation(@PathVariable String userId) {
        return null;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String userId) {
        return null;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String userId) {
        return null;
    }



}
