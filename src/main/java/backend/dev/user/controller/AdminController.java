package backend.dev.user.controller;

import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.DTO.admin.AdminJoinDTO;
import backend.dev.user.entity.AdminCode;
import backend.dev.user.entity.User;
import backend.dev.user.service.AdminService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return adminService.findUserList();
    }

    @GetMapping("/super/admins")
    public List<User> findAllAdmins() {
        return adminService.findAdminList();
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinAdmin(AdminJoinDTO adminJoinDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new PublicPlusCustomException(ErrorCode.NOT_EMPTY);
        }
        if (adminService.joinAdmin(adminJoinDTO)) {
            return ResponseEntity.noContent().build();
        }
        throw new PublicPlusCustomException(ErrorCode.SERVER_ERROR);
    }

    @GetMapping("/super/code")
    public ResponseEntity<List<AdminCode>> generateCode() {
        if (adminService.generateCode()) {
            return ResponseEntity.ok(adminService.findAdminCodeList());
        }
        throw new PublicPlusCustomException(ErrorCode.SERVER_ERROR);
    }

    @DeleteMapping("/super/admin/{userId}")
    public ResponseEntity<Map<String, String>> deleteAdmin(@PathVariable String userId) {
        adminService.deleteAdmin(userId);
        Map<String, String> responseMap = Map.of("message", "관리자 삭제 완료");
        return ResponseEntity.status(200).body(responseMap);
    }
}
