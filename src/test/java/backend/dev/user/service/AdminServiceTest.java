package backend.dev.user.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.DTO.admin.AdminJoinDTO;
import backend.dev.user.DTO.users.UserJoinDTO;
import backend.dev.user.entity.AdminCode;
import backend.dev.user.entity.Role;
import backend.dev.user.entity.User;
import backend.dev.user.repository.CodeRepository;
import backend.dev.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AdminServiceTest {

    @Autowired
    AdminService adminService;
    @Autowired
    CodeRepository codeRepository;
    @Autowired
    UserService userService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        AdminCode adminCode1 = new AdminCode("111");
        AdminCode adminCode2 = new AdminCode("222");
        AdminCode adminCode3 = new AdminCode("333");
        AdminCode adminCode4 = new AdminCode("444");
        List<AdminCode> adminCodeList = List.of(adminCode1, adminCode2, adminCode3,adminCode4);

        codeRepository.saveAll(adminCodeList);
        AdminJoinDTO adminJoinDTO1 = new AdminJoinDTO("aaa@aaa.com", "password123", "password123", "관리자1", "111");
        AdminJoinDTO adminJoinDTO2 = new AdminJoinDTO("bbb@bbb.com", "password123", "password123", "관리자2", "222");
        AdminJoinDTO adminJoinDTO3 = new AdminJoinDTO("ccc@ccc.com", "password123", "password123", "관리자3", "333");
        adminService.joinAdmin(adminJoinDTO1);
        adminService.joinAdmin(adminJoinDTO2);
        adminService.joinAdmin(adminJoinDTO3);
        UserJoinDTO userJoinDTO1 = new UserJoinDTO("ddd@ddd.com","password123","password123","테스트1");
        UserJoinDTO userJoinDTO2 = new UserJoinDTO("eee@eee.com","password123","password123","테스트1");
        userService.join(userJoinDTO1);
        userService.join(userJoinDTO2);
    }
    @Test
    void findUserList() {
        //when
        List<User> userList = adminService.findUserList();
        //then
        for (User user : userList) {
            Role role = user.getRole();
            assertThat(role).isSameAs(Role.USER);
        }
    }

    @Test
    void findAdminList() {
        List<User> adminList = adminService.findAdminList();
        for (User admin : adminList) {
            Role role = admin.getRole();
            assertThat(role).isSameAs(Role.ADMIN);
        }
    }

    @Test
    void joinAdmin() {
        AdminJoinDTO adminJoinFailByNotExistCode = new AdminJoinDTO("fff@fff.com","password123","password123","관리자4","333");
        AdminJoinDTO adminJoinSuccess = new AdminJoinDTO("fff@fff.com","password123","password123","관리자4","444");

        //when
        adminService.joinAdmin(adminJoinSuccess);
        AdminCode adminCodeByCode = codeRepository.findAdminCodeByCode("444");
        //then
        assertThatThrownBy(() -> adminService.joinAdmin(adminJoinFailByNotExistCode)).isInstanceOf(PublicPlusCustomException.class);
        assertThat(adminCodeByCode).isNull();

    }

    @Test
    void findAdminCodeList() {
        //given
        AdminCode adminCode0 = new AdminCode("111");
        AdminCode adminCode1 = new AdminCode("222");
        AdminCode adminCode2 = new AdminCode("333");
        codeRepository.saveAll(List.of(adminCode0, adminCode2, adminCode1));
        //when
        List<AdminCode> adminCodeList = adminService.findAdminCodeList();
        //then
        assertThat(adminCodeList).contains(adminCode0, adminCode1, adminCode2);
    }

    @Test
    void generateCode() {
        assertThat(adminService.generateCode()).isTrue();
        List<AdminCode> adminCodeList = adminService.findAdminCodeList();
        assertThat(adminCodeList.size()).isEqualTo(11);

    }

    @Test
    void deleteAdmin() {
        Optional<User> userByEmail = userService.findUserByEmail("aaa@aaa.com");
        String userId = null;
        if (userByEmail.isPresent()) {
            User findUser = userByEmail.get();
            userId = findUser.getId();
            adminService.deleteAdmin(userId);
        }
        assertThat(userId).isNotNull();
        User deletedUser = userRepository.findById(userId).orElse(null);
        assertThat(deletedUser).isNull();

    }
}