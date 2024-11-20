package backend.dev.user.service;

import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.DTO.ChangePasswordDTO;
import backend.dev.user.DTO.UserDTO;
import backend.dev.user.DTO.UserChangeInfoDTO;
import backend.dev.user.DTO.UserJoinDTO;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    @Value("${file.dir}")
    private String uploadPath;

    public UserDTO join(UserJoinDTO userJoinDTO) {
        String userid = UUID.randomUUID().toString();
        User user = User.builder()
                .userid(userid)
                .email(userJoinDTO.email())
                .password(userJoinDTO.password())
                .nickname(userJoinDTO.nickname())
                .build();
        userRepository.save(user);

        return User.of(user);
    }
    @Transactional(readOnly = true)
    public UserDTO findMyInformation(String userId) {
        return User.of(findUser(userId));
    }
    @Transactional(readOnly = true)
    public UserDTO findUserByEmail(String email) {
        return User.of(findUser(email));
    }

    public void changePassword(String userid, ChangePasswordDTO changePasswordDTO) {
        User user = findUser(userid);
        if(!changePasswordDTO.isSame()) throw new PublicPlusCustomException(ErrorCode.NOT_MATCH_PASSWORD);
        user.changePassword(changePasswordDTO.changePassword());
    }

    public void changeProfile(String userId, MultipartFile file) throws IOException {
        User user = findUser(userId);
        validate(file);
        String fullpath = uploadPath + file.getOriginalFilename();
        file.transferTo(new File(fullpath));
        user.changeProfile(fullpath);
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
        if (file == null) {
            throw new PublicPlusCustomException(ErrorCode.PROFILE_INVALID_FILE);
        }

        if (!file.getContentType().startsWith("image")) {
            throw new PublicPlusCustomException(ErrorCode.PROFILE_INVALID_FILE_TYPE);
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            throw new PublicPlusCustomException(ErrorCode.PROFILE_BLANK_FILE_NAME);
        }
    }
}
