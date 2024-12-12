package backend.dev.user.service;

import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.chatroom.entity.ChatParticipantRole;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import backend.dev.googlecalendar.service.CalenderService;
import backend.dev.notification.service.FCMService;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.setting.jwt.JwtAuthenticationProvider;
import backend.dev.setting.jwt.JwtToken;
import backend.dev.user.DTO.users.ChangePasswordDTO;
import backend.dev.user.DTO.users.UserChangeInfoDTO;
import backend.dev.user.DTO.users.UserDTO;
import backend.dev.user.DTO.users.UserJoinDTO;
import backend.dev.user.DTO.users.UserLoginDTO;
import backend.dev.user.entity.User;
import backend.dev.user.DTO.UserMapper;
import backend.dev.user.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
    private ChatParticipantRepository chatParticipantRepository;
    @Value("${file.dir}")
    private String uploadPath;
    @Value("${imageFile.path}")
    private String savePath;
    private final CalenderService calenderService;

    public void join(UserJoinDTO userJoinDTO) {
        if (userRepository.findByEmail(userJoinDTO.email()).isPresent()) {
            throw new PublicPlusCustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (userJoinDTO.isPasswordDifferent()) {
            throw new PublicPlusCustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        String encodedPassword = passwordEncoder.encode(userJoinDTO.password());
        User user = UserMapper.DtoToUser(userJoinDTO, encodedPassword);

        userRepository.save(user);
    }

    @Transactional(readOnly = false)
    public JwtToken login(UserLoginDTO userLoginDTO) {
        if (userLoginDTO.isPasswordEmpty()) {
            throw new PublicPlusCustomException(ErrorCode.PASSWORD_NOT_EMPTY);
        }

        User loginUser = userRepository.findByEmail(userLoginDTO.email())
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_MATCH_EMAIL_OR_PASSWORD));

        if (!passwordEncoder.matches(userLoginDTO.password(), loginUser.getPassword())) {
            throw new PublicPlusCustomException(ErrorCode.NOT_MATCH_EMAIL_OR_PASSWORD);
        }
//        loginUser.setFcmToken(userLoginDTO.fcmToken());
//        // FCM 토큰 검증 및 갱신
//        if (!fcmService.verifyToken(loginUser.getFcmToken())) {
//            fcmService.updateOrSaveToken(loginUser, userLoginDTO.fcmToken());
//        }

        return jwtAuthenticationProvider.makeToken(loginUser.getId());
    }

    public void logout(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer") && bearerToken.length() > 7) {
            String refreshToken = bearerToken.substring(7);
            jwtAuthenticationProvider.setTokenBlackList(refreshToken);

            SecurityContextHolder.clearContext();
        }
        throw new PublicPlusCustomException(ErrorCode.INVALID_TOKEN);
    }


    public JwtToken resignAccessTokenByHeader(String bearerRefreshToken) {
        if (bearerRefreshToken != null && bearerRefreshToken.startsWith("Bearer")) {
            String refreshToken = bearerRefreshToken.substring(7);
            return jwtAuthenticationProvider.resignAccessToken(refreshToken);
        }
        throw new PublicPlusCustomException(ErrorCode.INVALID_TOKEN);
    }

    public JwtToken resignAccessTokenByCookie(String refreshToken) {
        if (refreshToken != null) {
            return jwtAuthenticationProvider.resignAccessToken(refreshToken);
        }
        throw new PublicPlusCustomException(ErrorCode.INVALID_TOKEN);
    }

    @Transactional(readOnly = true)
    public UserDTO findMyInformation(String userId) {
        return UserMapper.userToDto(findUser(userId));
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void changePassword(String userid, ChangePasswordDTO changePasswordDTO) {
        if (!StringUtils.hasText(changePasswordDTO.changePassword())) {
            throw new PublicPlusCustomException(ErrorCode.PASSWORD_NOT_EMPTY);
        }
        if (changePasswordDTO.isPasswordDifferent()) {
            throw new PublicPlusCustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }
        User user = findUser(userid);
        user.changePassword(passwordEncoder.encode(changePasswordDTO.changePassword()));
    }

    public void changeProfile(String userId, MultipartFile file) throws IOException {
        User user = findUser(userId);

        makePath();
        validate(file);
        user.deleteProfile();

        String newFilename = makeSafeFilename(userId, file.getOriginalFilename());
        File destinationFile = Paths.get(uploadPath, newFilename).toAbsolutePath().toFile();

        log.info("사진 경로 : {}", destinationFile);
        file.transferTo(destinationFile);
        user.changeProfile(savePath + newFilename);
    }

    public void changeNickname(String userId, UserChangeInfoDTO userChangeInfoDTO) {
        User user = findUser(userId);
        if (userChangeInfoDTO.isBadNickName()) {
            throw new PublicPlusCustomException(ErrorCode.BAD_NICKNAME);
        }
        user.changeNickname(userChangeInfoDTO.nickname());
    }

    public void changeDescription(String userId, UserChangeInfoDTO userChangeInfoDTO) {
        User user = findUser(userId);
        user.changeDescription(userChangeInfoDTO.description());
    }

    public void deleteUser(String userId) {
        User user = findUser(userId);
        userRepository.delete(user);
    }

    private User findUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));
    }

    private void validate(MultipartFile file) {
        if (file == null || (file.getContentType() != null && !file.getContentType().startsWith("image"))) {
            throw new PublicPlusCustomException(ErrorCode.PROFILE_INVALID_FILE);
        }
    }

    private void makePath() {
        File uploadDir = new File(Paths.get(uploadPath).toAbsolutePath().toString());
        if (uploadDir.exists() || uploadDir.mkdirs()) {
            return;
        }
        throw new PublicPlusCustomException(ErrorCode.PROFILE_CREATE_DIRECTORY_FAIL);
    }

    private String makeSafeFilename(String userId, String originalFilename) {
        String safeFilename =
                originalFilename != null ? originalFilename.replaceAll("[^a-zA-Z0-9.\\-_]", "_") : "default";
        return userId + "_" + safeFilename;
    }

    // 참여자 추가 메서드
    public void addParticipantToUser(String userId, ChatParticipantRole role) {
        // 1. userId로 사용자 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));

        // 2. Participant 생성
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .user(user)
                .role(role) // 전달받은 Role 할당
                .build();

        // 3. 저장
        chatParticipantRepository.save(chatParticipant);
    }
}
