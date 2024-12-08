package backend.dev.activity.service;

import backend.dev.activity.dto.ActivityRequestDTO;
import backend.dev.activity.dto.ActivityResponseDTO;
import backend.dev.activity.entity.Activity;
import backend.dev.activity.entity.ActivityParticipants;
import backend.dev.activity.entity.ParticipantsRole;
import backend.dev.activity.exception.ActivityException;
import backend.dev.activity.exception.ActivityTaskException;
import backend.dev.activity.mapper.ActivityMapper;
import backend.dev.activity.repository.ActivityParticipantsRepository;
import backend.dev.activity.repository.ActivityRepository;
import backend.dev.googlecalendar.service.EventService;
import backend.dev.notification.dto.NotificationDTO;
import backend.dev.notification.entity.NotificationTitleType;
import backend.dev.notification.repository.NotificationRepository;
import backend.dev.notification.repository.TopicRepository;
import backend.dev.notification.service.NotificationService;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final EventService eventService;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final Pageable defaultPageable;
    private final ActivityParticipantsRepository activityParticipantsRepository;
    private final NotificationService notificationService;

    public ActivityResponseDTO readActivity(Long activityId){
        return ActivityMapper.toActivityResponseDTO(activityRepository.findById(activityId).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException));
    }
    public Page<ActivityResponseDTO> readAllActivities( ){
        return activityRepository.findAll(defaultPageable).map(ActivityMapper::toActivityResponseDTO);
    }
//    public Page<ActivityResponseDTO> readActivitiesByUserEmail(String userEmail){
//        return activityRepository.findByUser_Email(userEmail, defaultPageable).map(ActivityMapper::toActivityResponseDTO);
//    }
    public Page<ActivityResponseDTO> findByUserId(Pageable pageable){
        Page<Activity> activitiesByUserId = activityParticipantsRepository.findActivitiesByUserId(SecurityContextHolder.getContext().getAuthentication().getName(), pageable);
        return activitiesByUserId.map(ActivityMapper::toActivityResponseDTO);
    }

    public ActivityResponseDTO createActivity(ActivityRequestDTO dto) {
        // 1. Activity 저장
        Activity activity = ActivityMapper.toActivity(dto);
        // 2. User 정보에서 구글캘린더 Id를 찾는다.
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));

        activityRepository.save(activity);

        ActivityParticipants activityParticipantsAdmin = ActivityMapper.toActivityParticipantsAdmin(activity, user);

        activity.addParticipant(activityParticipantsAdmin);

        activityParticipantsRepository.save(activityParticipantsAdmin);
        // 2. 구글 캘린더 API로 이벤트 생성
//        String eventId = eventService.createEvent(dto).getEventId();
//
//        activity.changeGoogleEventId(eventId);

        activityRepository.save(activity);

        return ActivityMapper.toActivityResponseDTO(activity);
    }


    public ActivityResponseDTO updateActivity(ActivityRequestDTO dto, Long activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
        // 토큰에서 사용자 정보를 불러와 사용자 조회
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
        //
        updateIsPresent(dto.title(),activity::changeTitle);
        updateIsPresent(dto.description(),activity::changeDescription);
        updateIsPresent(dto.location(),activity::changeLocation);
        updateIsPresent(dto.maxParticipants(),activity::changeMaxParticipants);
        updateIsPresent(dto.startTime(), activity::changeStartTime);
        updateIsPresent(dto.endTime(), activity::changeEndTime);
        activityRepository.save(activity);

        return ActivityMapper.toActivityResponseDTO(activity);
    }
    private <T> void updateIsPresent(T value, Consumer<T> updateMethod){
        if (value != null) {
            updateMethod.accept(value);
        }
    }
    public void deleteActivity(Long activityId){
        Activity activity = activityRepository.findById(activityId).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
        // 유저 정보 받아오기



        activityRepository.deleteById(activityId);
        if (activityRepository.existsById(activityId)) {
            throw ActivityException.ACTIVITY_NOT_DELETED.getException();
        }

    }

    public ActivityResponseDTO JoinActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);

        if (activity.getMaxParticipants() == activity.getCurrentParticipants()){
            throw ActivityException.ACTIVITY_FULL.getException();
        }

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));

        ActivityParticipants activityParticipantsUser = ActivityMapper.toActivityParticipantsUser(activity, user);
        activity.addParticipant(activityParticipantsUser);

        ActivityParticipants byActivityAndRole = activityParticipantsRepository.findByActivityAndRole(activity, ParticipantsRole.ADMIN);
        User admin = byActivityAndRole.getUser();

        activityRepository.save(activity);
        activityParticipantsRepository.save(activityParticipantsUser);
        activity.changeCurrentParticipants(activity.getCurrentParticipants()+1);
        notificationService.toSendNotification(NotificationDTO.builder()
                .title("모임 참가 알림")
                .message(admin.getNickname() + NotificationTitleType.ACTIVITY_PARTICIPATE.getMessage())
                .build(),admin);

        return ActivityMapper.toActivityResponseDTO(activity);
    }

    public void activityQuit(Long activityId){
        Activity activity = activityRepository.findById(activityId).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));

        if (activityParticipantsRepository.existsByActivityAndUserId(activity,userId)) {
            ActivityParticipants byActivityAndRole = activityParticipantsRepository.findByActivityAndRole(activity, ParticipantsRole.ADMIN);
            User admin = byActivityAndRole.getUser();

            notificationService.toSendNotification(NotificationDTO.builder()
                    .title("모임 퇴장 알림")
                    .message(admin.getNickname() + NotificationTitleType.ACTIVITY_QUIT.getMessage())
                    .build(),admin);
            activityParticipantsRepository.deleteByUser(user);


        }else {
            throw ActivityException.ACTIVITY_NOT_FOUND.getException();
        }

        activity.changeCurrentParticipants(activity.getCurrentParticipants()-1);

    }
//    public ActivityResponseDTO updateActivity(Long activityId, ActivityUpdateDTO dto) {
//        // 1.유저 데이터를 세션에서 받아오고 검색
//        User user = userRepository.findByEmail("aaacd@example.com").orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));
//        return dto;
//    }
        //1.ex Activity
//        // 2. 고유 Topic Name 생성
//        String topicName = "activity_" + activity.getActivityId();
//        Topic topic = topicRepository.save(new Topic(topicName));
//
//        // 3. 사용자 기본 구독 처리 함수화
//        for (User user : dto.getParticipants()) {
//            FCMToken token = fcmTokenRepository.findByUser(user);
//            if (token != null) {
//                // TopicSubscription 생성
//                topicSubscriptionRepository.save(new TopicSubscription(token, topic));
//
//                // FCM 구독 요청
//                FirebaseMessaging.getInstance()
//                        .subscribeToTopic(Collections.singletonList(token.getFcm_token()), topicName);
//            }
//        }

//        return activity;
//    }
//    public ActivityRequestDTO read(Long activityId)  {
//        activityRepository.findById()
//
//    }
}
