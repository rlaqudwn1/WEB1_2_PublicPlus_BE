package backend.dev.user.DTO;

import backend.dev.user.DTO.admin.AdminJoinDTO;
import backend.dev.user.DTO.users.UserDTO;
import backend.dev.user.DTO.users.UserJoinDTO;
import backend.dev.user.entity.Role;
import backend.dev.user.entity.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserMapper {

    public static UserDTO userToDto(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getProfilePath(), user.getNickname(),
                user.getDescription(), user.getRole());
    }

    public static User DtoToUser(UserJoinDTO userJoinDTO, String encodedPassword) {
        String userid = UUID.randomUUID().toString();
        return User.builder()
                .userId(userid)
                .email(userJoinDTO.email())
                .password(encodedPassword)
//                .googleCalenderId(calenderService.createCalendar(userJoinDTO.nickname())) // 회원가입 할 경우 구글 캘린더 생성
                .nickname(userJoinDTO.nickname())
                .build();
    }

    public static User DtoToAdmin(AdminJoinDTO adminJoinDTO, String encodedPassword) {
        String userid = UUID.randomUUID().toString();
        return User.builder()
                .userId(userid)
                .email(adminJoinDTO.email())
                .password(encodedPassword)
                .nickname(adminJoinDTO.nickname())
                .role(Role.ADMIN)
                .build();
    }

}
