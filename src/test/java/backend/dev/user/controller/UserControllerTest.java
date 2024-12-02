package backend.dev.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.DTO.users.ChangePasswordDTO;
import backend.dev.user.DTO.users.UserChangeInfoDTO;
import backend.dev.user.DTO.users.UserDTO;
import backend.dev.user.DTO.users.UserJoinDTO;
import backend.dev.user.entity.User;
import backend.dev.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = {"classpath:/application-test.properties"})
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
    @DisplayName("중복이메일이거나, 암호확인 로직을 통과하지 못한 회원가입은 실패하고, 정상적인 회원가입은 성공한다")
    void join(){
        //given
        UserJoinDTO failJoinByDuplicate = new UserJoinDTO("aaa@aaa.com","password123","password","테스트1");
        //when,then
        assertThatThrownBy(() -> userController.join(failJoinByDuplicate)).isInstanceOf(PublicPlusCustomException.class); // 중복 이메일
        //given
        UserJoinDTO failJoinByDifferentPassword = new UserJoinDTO("ddd@ddd.com","password123","password","테스트4");
        //when,then
        assertThatThrownBy(()->userController.join(failJoinByDifferentPassword)).isInstanceOf(PublicPlusCustomException.class); // 비밀번호 확인로직 통과 실패
        //given
        UserJoinDTO successJoin = new UserJoinDTO("ddd@ddd.com","password123","password123","테스트4");
        //when
        userController.join(successJoin); // 가입 성공
        Optional<User> userByEmail = userService.findUserByEmail("ccc@ccc.com");
        //then
        assertThat(userByEmail.isPresent()).isTrue();
    }
    @Test
    @DisplayName("암호확인 로직을 통과하지 못한 암호변경은 실패하고, 로직을 통과한 암호변경은 성공한다")
    void changePassword(){
        //given
        ChangePasswordDTO failChangePassword = new ChangePasswordDTO("bbb@bbb.com", "password1", "password2");
        //when,then
        boolean alwaysTrue = userService.findUserByEmail("bbb@bbb.com").isPresent();
        if (!alwaysTrue){return;}
        User user = userService.findUserByEmail("bbb@bbb.com").get();
        assertThatThrownBy(() -> userController.changePassword(user.getId(), failChangePassword)).isInstanceOf(PublicPlusCustomException.class);
        //given
        ChangePasswordDTO successChangePassword = new ChangePasswordDTO("bbb@bbb.com", "password1", "password1");
        //when,then
        userController.changePassword(user.getId(), successChangePassword);
    }

    @Test
    @DisplayName("닉네임 도메인 확인로직을 통과하지 못한 닉네임은 변경에 예외가 발생하고, 통과한 닉네임은 변경에 성공한다.")
    void changeNickname() {
        boolean alwaysTrue = userService.findUserByEmail("bbb@bbb.com").isPresent();
        if (!alwaysTrue){return;}
        User user = userService.findUserByEmail("bbb@bbb.com").get();
        UserChangeInfoDTO failChangeInfoDTO = new UserChangeInfoDTO("BBB",null);
        assertThatThrownBy(()->userController.updateNickname(user.getId(), failChangeInfoDTO)).isInstanceOf(PublicPlusCustomException.class);
        UserChangeInfoDTO successChangeInfoDTO = new UserChangeInfoDTO("닉네임변경성공",null);
        userController.updateNickname(user.getId(), successChangeInfoDTO);
        UserDTO myInformation = userService.findMyInformation(user.getId());
        assertThat(myInformation.nickname()).isEqualTo("닉네임변경성공");
    }
    @Test
    @DisplayName("소개글 변경에 성공한다")
    void changeDescription() {
        //given
        boolean alwaysTrue = userService.findUserByEmail("bbb@bbb.com").isPresent();
        if (!alwaysTrue){return;}
        User user = userService.findUserByEmail("bbb@bbb.com").get();
        UserChangeInfoDTO changeInfoDTO = new UserChangeInfoDTO("BBB","반갑습니다");
        userController.updateDescription(user.getId(),changeInfoDTO);
        UserDTO myInformation = userService.findMyInformation(user.getId());
        assertThat(myInformation.description()).isEqualTo("반갑습니다");
    }

}