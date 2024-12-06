package backend.dev.likes.repository;

import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.repository.FacilityDetailsRepository;
import backend.dev.likes.entity.Likes;
import backend.dev.likes.exception.LikeException;
import backend.dev.likes.exception.LikeTaskException;
import backend.dev.testdata.FacilityInitializer;
import backend.dev.user.DTO.users.UserJoinDTO;
import backend.dev.user.entity.User;
import backend.dev.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(FacilityInitializer.class) // FacilityInitializer를 테스트 클래스에 임포트
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private FacilityDetailsRepository facilityDetailsRepository;

    @BeforeAll
    static void init(
            @Autowired
            UserService userService
    ){
        UserJoinDTO userJoinDTO1 = new UserJoinDTO("bbb@bbb.com","password123","password123","테스트1");
        UserJoinDTO userJoinDTO2 = new UserJoinDTO("ccc@ccc.com","password123","password123","테스트1");
        userService.join(userJoinDTO1);
        userService.join(userJoinDTO2);
    }
    @Test
    @Order(1)
    @DisplayName("좋아요 추가")
    void like(){
        //given
        User user = userService.findUserByEmail("bbb@bbb.com").get();
        FacilityDetails fac1 = facilityDetailsRepository.findById("FAC1").get();

        //when
        if (likeRepository.existsByUserAndFacility(user, fac1)){
            throw LikeException.DUPLICATE_LIKE.getLikeException();
        }else {
            Likes likes = Likes.builder()
                    .user(user)
                    .facility(fac1)
                    .build();
            likeRepository.save(likes);
        }

        //then
        assertEquals(1,likeRepository.count());
    }

    @Test
    @Order(2)
    @DisplayName("좋아요 취소")
    @Transactional
    void dislike(){
        //given
        User user = userService.findUserByEmail("bbb@bbb.com").get();
        FacilityDetails fac1 = facilityDetailsRepository.findById("FAC1").get();
        //when
        likeRepository.deleteByUserAndFacility(user, fac1);
        //then
        assertEquals(0,likeRepository.count());
    }


}