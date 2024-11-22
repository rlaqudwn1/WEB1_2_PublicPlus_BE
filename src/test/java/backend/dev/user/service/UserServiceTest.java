package backend.dev.user.service;

import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.DTO.UserJoinDTO;
import backend.dev.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    private UserService userService;

    @BeforeEach
    void init(){
        UserJoinDTO userJoinDTO1 = new UserJoinDTO("bbb@bbb.com","password123","password123","테스트1");
        UserJoinDTO userJoinDTO2 = new UserJoinDTO("ccc@ccc.com","password123","password123","테스트1");
        userService.join(userJoinDTO1);
        userService.join(userJoinDTO2);
    }

    @Test
    @DisplayName("프로필 업로드 실패 테스트")
    void changeProfile() throws IOException {
        User user = userService.findUserByEmail("bbb@bbb.com").get();
        MockMultipartFile failMultipartFileNoImage = new MockMultipartFile("file", "test-fail.txt", "text/plain", "hello, world".getBytes());
        Assertions.assertThatThrownBy(() -> userService.changeProfile(user.getId(), failMultipartFileNoImage)).isInstanceOf(PublicPlusCustomException.class);

        MockMultipartFile failMultipartFileNull = null;
        Assertions.assertThatThrownBy(() -> userService.changeProfile(user.getId(), failMultipartFileNull)).isInstanceOf(PublicPlusCustomException.class);

    }

}