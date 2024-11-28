package backend.dev.user.controller;

import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.DTO.ChangePasswordDTO;
import backend.dev.user.DTO.UserChangeInfoDTO;
import backend.dev.user.DTO.UserDTO;
import backend.dev.user.DTO.UserJoinDTO;
import backend.dev.user.entity.User;
import backend.dev.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserControllerTest {

    @Autowired
    private UserController userController;
    @Autowired
    private UserService userService;

    @BeforeEach
    void init() {
        //given
        UserJoinDTO userJoinDTO1 = new UserJoinDTO("aaa@aaa.com","password123","password123","테스트1");
        UserJoinDTO userJoinDTO2 = new UserJoinDTO("bbb@bbb.com","password123","password123","테스트2");
        UserJoinDTO userJoinDTO3 = new UserJoinDTO("ccc@ccc.com","password123","password123","테스트3");
        userController.join(userJoinDTO1);
        userController.join(userJoinDTO2);
        userController.join(userJoinDTO3);
    }

    @Test
    @DisplayName("회원가입 성공 및 실패 테스트")
    void join(){
        //given
        UserJoinDTO failJoinByDifferentPassword = new UserJoinDTO("ddd@ddd.com","password123","password","테스트4");
        //when,then
        assertThatThrownBy(()->userController.join(failJoinByDifferentPassword)).isInstanceOf(PublicPlusCustomException.class);
        //given
        UserJoinDTO successJoin = new UserJoinDTO("ddd@ddd.com","password123","password123","테스트4");
        //when
        userController.join(successJoin);
        Optional<User> userByEmail = userService.findUserByEmail("ccc@ccc.com");
        //then
        assertThat(userByEmail.get()).isNotNull();
    }
    @Test
    @DisplayName("암호 변경 성공 및 실패 테스트")
    void changePassword(){
        //given
        ChangePasswordDTO failChangePassword = new ChangePasswordDTO("bbb@bbb.com", "password1", "password2");
        //when,then
        User user = userService.findUserByEmail("bbb@bbb.com").get();
        assertThatThrownBy(() -> userController.changePassword(user.getId(), failChangePassword)).isInstanceOf(PublicPlusCustomException.class);
        //given
        ChangePasswordDTO successChangePassword = new ChangePasswordDTO("bbb@bbb.com", "password1", "password1");
        //when,then
        userController.changePassword(user.getId(), successChangePassword);
    }

    @Test
    @DisplayName("닉네임 변경 성공 및 실패 테스트")
    void changeNickname() {
        User user = userService.findUserByEmail("bbb@bbb.com").get();
        UserChangeInfoDTO failChangeInfoDTO = new UserChangeInfoDTO("BBB",null);
        assertThatThrownBy(()->userController.updateNickname(user.getId(), failChangeInfoDTO)).isInstanceOf(PublicPlusCustomException.class);
        UserChangeInfoDTO successChangeInfoDTO = new UserChangeInfoDTO("닉네임변경성공",null);
        userController.updateNickname(user.getId(), successChangeInfoDTO);
        UserDTO myInformation = userService.findMyInformation(user.getId());
        assertThat(myInformation.nickname()).isEqualTo("닉네임변경성공");
    }
    @Test
    @DisplayName("소개글 변경 테스트")
    void changeDescription() {
        //given
        User user = userService.findUserByEmail("bbb@bbb.com").get();
        UserChangeInfoDTO changeInfoDTO = new UserChangeInfoDTO("BBB","반갑습니다");
        userController.updateDescription(user.getId(),changeInfoDTO);
        UserDTO myInformation = userService.findMyInformation(user.getId());
        assertThat(myInformation.description()).isEqualTo("반갑습니다");
    }

}