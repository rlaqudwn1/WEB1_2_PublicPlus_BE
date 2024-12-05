package backend.dev.activity.service;


import backend.dev.activity.entity.Activity;
import backend.dev.activity.entity.ActivityParticipants;
import backend.dev.activity.entity.ParticipantsRole;
import backend.dev.activity.exception.ActivityException;
import backend.dev.activity.mapper.ActivityMapper;
import backend.dev.activity.repository.ActivityParticipantsRepository;
import backend.dev.activity.repository.ActivityRepository;
import backend.dev.googlecalendar.service.EventService;
import backend.dev.user.DTO.users.UserJoinDTO;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import backend.dev.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActivityParticipantsTests {
    private static final Logger log = LoggerFactory.getLogger(ActivityParticipantsTests.class);
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
    private ActivityParticipantsRepository activityParticipantsRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private Pageable defaultPageable;


    @BeforeAll
    static void init(@Autowired UserRepository userRepository,
                     @Autowired ActivityRepository activityRepository,
                     @Autowired UserService userService,
                     @Autowired ActivityMapper activityMapper,
                     @Autowired ActivityParticipantsRepository activityParticipantsRepository) {

        userRepository.deleteAll();
        activityRepository.deleteAll();

        UserJoinDTO userJoinDTO1 = new UserJoinDTO("bbb@bbb.com", "password123", "password123", "테스트1");
        UserJoinDTO userJoinDTO2 = new UserJoinDTO("ccc@ccc.com", "password123", "password123", "테스트1");
        userService.join(userJoinDTO1);
        userService.join(userJoinDTO2);

        User user = userRepository.findByEmail("bbb@bbb.com").get();
        User user2 = userRepository.findByEmail("ccc@ccc.com").get();

        Activity activity = Activity.builder()
                .description("테스트 입니다")
                .title("테스트 타이틀")
                .location("테스트 장소")
                .maxParticipants(10)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusMinutes(30))
                .build();
        Activity activity2 = Activity.builder()
                .startTime(ActivityMapper.parseDateTime("2024-11-07T15:00:00Z"))
                .endTime(ActivityMapper.parseDateTime("2024-11-08T15:00:00Z"))
                .description("테스트 입니다2")
                .title("테스트 타이틀2")
                .location("테스트 장소2")
                .maxParticipants(10)
                .build();
        activityRepository.save(activity);
        activityRepository.save(activity2);

    }
    @Autowired
    private EntityManager entityManager;

    @Test
    @Order(1)
    @DisplayName("모임 생성 테스트")
    @Transactional
    void activityCreate() {
        //given
        User user = userRepository.findByEmail("bbb@bbb.com").orElseThrow();
        User user2 = userRepository.findByEmail("ccc@ccc.com").orElseThrow();
        Activity activity = activityRepository.findById(1L).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
        Activity activity2 = activityRepository.findById(2L).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);

        //when
        ActivityParticipants activityParticipants = ActivityParticipants.builder()
                .user(user)
                .role(ParticipantsRole.ADMIN)
                .activity(activity)
                .build();
        activityParticipantsRepository.save(activityParticipants);
        activity.addParticipant(activityParticipants);
        user.getActivityParticipants().add(activityParticipants);

        ActivityParticipants activityParticipants2 = ActivityParticipants.builder()
                .user(user2)
                .role(ParticipantsRole.ADMIN)
                .activity(activity2)
                .build();
        activityParticipantsRepository.save(activityParticipants2);
        activity2.addParticipant(activityParticipants2);
        user2.getActivityParticipants().add(activityParticipants2);

        activityRepository.save(activity);
        activityRepository.save(activity2);
        userRepository.save(user);
        userRepository.save(user2);

        entityManager.flush();
        entityManager.clear();

        //then
        Activity savedActivity = activityRepository.findById(1L).orElseThrow();
        Activity savedActivity2 = activityRepository.findById(2L).orElseThrow();
        User savedUser = userRepository.findByEmail("bbb@bbb.com").orElseThrow();
        User savedUser2 = userRepository.findByEmail("ccc@ccc.com").orElseThrow();

        assertEquals(1, savedActivity.getParticipants().size());
        assertEquals(1, savedActivity2.getParticipants().size());
        assertEquals(1, savedUser.getActivityParticipants().size());
        assertEquals(1, savedUser2.getActivityParticipants().size());
    }

    @Test
    @Order(2)
    @DisplayName("활동 참가 테스트 및 영속성 검증")
    @Transactional
    void activityParticipantsJoinTest() {
        // given
        User user = userRepository.findByEmail("bbb@bbb.com").orElseThrow();
        Activity activity = activityRepository.findById(1L).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
        Activity activity2 = activityRepository.findById(2L).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);

        log.info("Initial Participants for Activity 1: {}", activity.getParticipants().size());
        log.info("Initial Participants for Activity 2: {}", activity2.getParticipants().size());

        // 참가자 생성 및 추가
        ActivityParticipants participant1 = ActivityParticipants.builder()
                .user(user)
                .role(ParticipantsRole.USER)
                .activity(activity)
                .build();
        ActivityParticipants participant2 = ActivityParticipants.builder()
                .user(user)
                .role(ParticipantsRole.USER)
                .activity(activity2)
                .build();

        activityParticipantsRepository.save(participant1);
        activityParticipantsRepository.save(participant2);

        activity.addParticipant(participant1);
        activity2.addParticipant(participant2);

        activityRepository.save(activity);
        activityRepository.save(activity2);

        // when
        Page<Activity> activitiesByUserId = activityParticipantsRepository.findActivitiesByUserId(user.getId(),defaultPageable);
        log.info("Activities by User ID: {}", activitiesByUserId.getTotalElements());

        // then
        assertEquals(1, activity.getParticipants().size());
        assertEquals(1, activity2.getParticipants().size());
        assertEquals(2, activitiesByUserId.getTotalElements());

        log.info("Test completed successfully");
    }




}
