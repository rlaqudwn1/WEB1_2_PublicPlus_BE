package backend.dev.activity.service;

import backend.dev.activity.dto.ActivityRequestDTO;
import backend.dev.activity.entity.Activity;
import backend.dev.activity.exception.ActivityException;
import backend.dev.activity.mapper.ActivityMapper;
import backend.dev.activity.repository.ActivityRepository;
//import backend.dev.testdata.ActivityInitializer;
import backend.dev.googlecalendar.service.EventService;
import backend.dev.user.DTO.UserJoinDTO;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import backend.dev.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Import(ActivityInitializer.class) // FacilityInitializer를 테스트 클래스에 임포트
public class ActivityServiceTests {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private EventService eventService;
    @Autowired
    private Pageable defaultPageable;

    @BeforeAll
    static void init(@Autowired UserRepository userRepository,
                     @Autowired ActivityRepository activityRepository,
                     @Autowired UserService userService,
                     @Autowired ActivityMapper activityMapper) {
        // 데이터 초기화
        userRepository.deleteAll();
        activityRepository.deleteAll();

        UserJoinDTO userJoinDTO1 = new UserJoinDTO("bbb@bbb.com", "password123", "password123", "테스트1");
        UserJoinDTO userJoinDTO2 = new UserJoinDTO("ccc@ccc.com", "password123", "password123", "테스트1");
        userService.join(userJoinDTO1);
        userService.join(userJoinDTO2);

        User user = userRepository.findByEmail("bbb@bbb.com").get();

        Activity activity = Activity.builder()
                .user(user)
                .description("테스트 입니다")
                .title("테스트 타이틀")
                .location("테스트 장소")
                .maxParticipants(10)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(30))
                .googleEventId("uccf160d756gbkol9ncbjm9q44")
                .build();
        Activity activity2 = Activity.builder()
                .user(user)
                .startTime(ActivityMapper.parseDateTime("2024-11-07T15:00:00Z"))
                .endTime(ActivityMapper.parseDateTime("2024-11-08T15:00:00Z"))
                .description("테스트 입니다2")
                .title("테스트 타이틀2")
                .location("테스트 장소2")
                .maxParticipants(10)
                .googleEventId("i3jjae75dd0u8q2plutff32b40")
                .build();
        activityRepository.save(activity);
        activityRepository.save(activity2);
    }

    @Test
    @DisplayName("모임 업데이트 테스트입니다")
    @Order(1)
    void ActivityUpdateTest(){
        //given 알람 업데이트 DTO가 주어졌을 경우
        Long activityId = 1L;

        ActivityRequestDTO updateDTO = ActivityRequestDTO.builder()
                .description("업데이트 설명")
                .title("업데이트 중")
                .build();
        //when
        Activity activity = activityRepository.findById(activityId).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
        activity.changeDescription(updateDTO.description());
        activity.changeTitle(updateDTO.title());

        //then
        Assertions.assertEquals(updateDTO.description(), activity.getDescription(),"설명이 업데이트 중이라고 바뀌어야 합니다");
        Assertions.assertEquals(updateDTO.title(), activity.getTitle(),"제목이 업데이트 설명으로 바뀌어야 합니다");
    }
    @Test
    @DisplayName("유저 이름으로 모임 불러오기 테스트")
    void getByUserEmail(){
        //given
        User user = userRepository.findByEmail("bbb@bbb.com").get();

        //when
        Page<Activity> byUserEmail = activityRepository.findByUser_Email("bbb@bbb.com", defaultPageable);
        //then
        for (Activity activity : byUserEmail) {
            System.out.println(activity);
        }
        assert byUserEmail.getTotalElements() > 0;
    }
    @Test
    @DisplayName("유저 이름으로 모임 불러오기 실패 테스트")
    void getByUserFail(){
        //given 모임이 없는 유저 
        User user = userRepository.findByEmail("ccc@ccc.com").get();
        //when
        Page<Activity> byUserEmail = activityRepository.findByUser_Email(user.getEmail(), defaultPageable);
        //then
        Assertions.assertFalse(byUserEmail.getTotalElements() > 0);
    }
    @Test
    @DisplayName("모임 삭제 테스트")
    void ActivityDeleteTest(){
        //given 삭제할 아이디
        Long activityId = 2L;
        Activity activity = activityRepository.findById(activityId).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
        //when activity를 삭제 할 경우
        activityRepository.delete(activity);
        //then 모임이 삭제되었는지 테스트
        Assertions.assertFalse(activityRepository.existsById(activityId));
    }
}
