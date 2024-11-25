//package backend.dev.notification.controller;
//
//import backend.dev.user.entity.User;
//import backend.dev.user.repository.UserRepository;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/tokens")
//public class TokenController {
//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping("/save")
//    public ResponseEntity<String> saveToken(@RequestBody TokenRequest tokenRequest) {
//        try {
//            User user = userRepository.findById(tokenRequest.getUserId())
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//            user.(tokenRequest.getToken());
//            userRepository.save(user);
//            return ResponseEntity.ok("Token saved successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving token");
//        }
//    }
//}
//
//@Data
//class TokenRequest {
//    private Long userId;
//    private String token;
//}
