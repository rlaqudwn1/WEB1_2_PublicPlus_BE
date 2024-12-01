package backend.dev.user.service;

import backend.dev.googlecalendar.service.CalenderService;
import backend.dev.notification.exception.NotificationException;
import backend.dev.notification.exception.NotificationTaskException;
import backend.dev.notification.service.FCMService;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.setting.jwt.JwtAuthenticationProvider;
import backend.dev.setting.jwt.JwtToken;
import backend.dev.user.DTO.ChangePasswordDTO;
import backend.dev.user.DTO.UserChangeInfoDTO;
import backend.dev.user.DTO.UserDTO;
import backend.dev.user.DTO.UserJoinDTO;
import backend.dev.user.DTO.UserLoginDTO;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final FCMService fcmService;
    @Value("${file.dir}")
    private String uploadPath;
    private final CalenderService calenderService;

    public void join(UserJoinDTO userJoinDTO) {
        //이메일 중복 검사
        if(userRepository.findByEmail(userJoinDTO.email()).isPresent()) throw new PublicPlusCustomException(ErrorCode.DUPLICATE_EMAIL);

        String userid = UUID.randomUUID().toString();
        try{  User user = User.builder()
                .userId(userid)
                .email(userJoinDTO.email())
                .password(passwordEncoder.encode(userJoinDTO.password()))
                .googleCalenderId(calenderService.createCalendar(userJoinDTO.nickname())) // 회원가입 할 경우 구글 캘린더 생성
                .nickname(userJoinDTO.nickname())
                .build();
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @Transactional(readOnly = false)
    public JwtToken login(UserLoginDTO userLoginDTO) {
        if(!userLoginDTO.checkPassword()) throw new PublicPlusCustomException(ErrorCode.NOT_MATCH_EMAIL_OR_PASSWORD);
        User loginUser = userRepository.findByEmail(userLoginDTO.email())
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_MATCH_EMAIL_OR_PASSWORD));
        if(!passwordEncoder.matches(userLoginDTO.password(), loginUser.getPassword())) throw new PublicPlusCustomException(ErrorCode.NOT_MATCH_EMAIL_OR_PASSWORD);

        // FCM 토큰 검증 및 갱신
        if (!fcmService.verifyToken(loginUser.getFcmToken())) {
            fcmService.updateOrSaveToken(loginUser, userLoginDTO.fcmToken());
        }
        return jwtAuthenticationProvider.makeToken(loginUser.getId());
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public JwtToken resignAccessTokenByHeader(String bearerRefreshToken) {
        if (!(bearerRefreshToken != null && bearerRefreshToken.startsWith("Bearer") && bearerRefreshToken.length() > 7)) {
            throw new PublicPlusCustomException(ErrorCode.INVALID_TOKEN);
        }
        String refreshToken = bearerRefreshToken.substring(7);
        return jwtAuthenticationProvider.resignAccessToken(refreshToken);

    }
    public JwtToken resignAccessTokenByCookie(String refreshToken) {
        if (refreshToken != null) {
            return jwtAuthenticationProvider.resignAccessToken(refreshToken);
        }
        throw new PublicPlusCustomException(ErrorCode.INVALID_TOKEN);
    }

    @Transactional(readOnly = true)
    public UserDTO findMyInformation(String userId) {
        return User.of(findUser(userId));
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void changePassword(String userid, ChangePasswordDTO changePasswordDTO) {
        User user = findUser(userid);
        if(!changePasswordDTO.isSame()) throw new PublicPlusCustomException(ErrorCode.NOT_MATCH_PASSWORD);
        user.changePassword(changePasswordDTO.changePassword());
    }

    public void changeProfile(String userId, MultipartFile file) throws IOException {
        User user = findUser(userId);
        makePath();
        validate(file);
        user.deleteProfile();
        String newFilename = makeSafeFilename(userId,file.getOriginalFilename());
        File destinationFile = Paths.get(uploadPath, newFilename).toAbsolutePath().toFile();
        log.info("사진 경로 : {}",destinationFile);
        file.transferTo(destinationFile);
        user.changeProfile(destinationFile.getAbsolutePath());
    }

    public void changeNickname(String userId, UserChangeInfoDTO userChangeInfoDTO) {
        User user = findUser(userId);
        if (userChangeInfoDTO.nickname() != null) {
            if(!userChangeInfoDTO.checkNickname()) throw new PublicPlusCustomException(ErrorCode.BAD_NICKNAME);
            user.changeNickname(userChangeInfoDTO.nickname());
        }
    }

    public void changeDescription(String userId, UserChangeInfoDTO userChangeInfoDTO) {
        User user = findUser(userId);
        user.changeDescription(userChangeInfoDTO.description());
    }

    public void deleteUser(String userId){
        User user = findUser(userId);
        userRepository.delete(user);
    }

    private User findUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));
    }

    private void validate(MultipartFile file) {
        if (file == null||(file.getContentType()!=null&&!file.getContentType().startsWith("image"))) {
            throw new PublicPlusCustomException(ErrorCode.PROFILE_INVALID_FILE);
        }
    }

    private void makePath() {
        File uploadDir = new File(Paths.get(uploadPath).toAbsolutePath().toString());
        if (!uploadDir.exists()) {
            if (!uploadDir.mkdirs()) {
                throw new PublicPlusCustomException(ErrorCode.PROFILE_CREATE_DIRECTORY_FAIL);
            }
        }
    }

    private String makeSafeFilename(String userId, String originalFilename) {
        String safeFilename = originalFilename!=null ? originalFilename.replaceAll("[^a-zA-Z0-9.\\-_]", "_")  : "default";
        return userId +"_"+safeFilename;
    }
}
