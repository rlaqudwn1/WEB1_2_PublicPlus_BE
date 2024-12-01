package backend.dev.activity.service;

import backend.dev.activity.dto.ActivityRequestDTO;
import backend.dev.activity.dto.ActivityResponseDTO;
import backend.dev.activity.entity.Activity;
import backend.dev.activity.exception.ActivityException;
import backend.dev.activity.mapper.ActivityMapper;
import backend.dev.activity.repository.ActivityRepository;
import backend.dev.googlecalendar.service.EventService;
import backend.dev.notification.repository.TopicRepository;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final EventService eventService;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    //String topicName = "activity_" + activityId; 으로 토픽을 고유하게 저장
    private final Pageable defaultPageable;

    public ActivityResponseDTO readActivity(Long activityId){
        return ActivityMapper.toActivityResponseDTO(activityRepository.findById(activityId).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException));
    }
    public Page<ActivityResponseDTO> readAllActivities( ){
        return activityRepository.findAll(defaultPageable).map(ActivityMapper::toActivityResponseDTO);
    }
    public Page<ActivityResponseDTO> readActivitiesByUserEmail(String userEmail){
        return activityRepository.findByUser_Email(userEmail, defaultPageable).map(ActivityMapper::toActivityResponseDTO);
    }

    public ActivityResponseDTO createActivity(ActivityRequestDTO dto, String email) {
        // 1. Activity 저장
        Activity activity = ActivityMapper.toActivity(dto);
        // 2. User 정보에서 구글캘린더 Id를 찾는다.
        User user = userRepository.findByEmail(email).orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));
        // User 정보를 받고 처리한다
        activity.changeUser(user);
        // activity 생성
        activityRepository.save(activity);

        // 2. 구글 캘린더 API로 이벤트 생성
        String eventId = eventService.createEvent(dto,user.getGoogleCalenderId());

        activity.changeGoogleEventId(eventId);

        activityRepository.save(activity);

        return ActivityMapper.toActivityResponseDTO(activity);
    }

    public ActivityResponseDTO updateActivity(ActivityRequestDTO dto, Long activityId,String email) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
        // 토큰에서 사용자 정보를 불러와 사용자 조회
        User user = userRepository.findByEmail(email).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
        //
        updateIsPresent(dto.title(),activity::changeTitle);
        updateIsPresent(dto.description(),activity::changeDescription);
        updateIsPresent(dto.location(),activity::changeLocation);
        updateIsPresent(dto.maxParticipants(),activity::changeMaxParticipants);
        updateIsPresent(dto.startTime(), activity::changeStartTime);
        updateIsPresent(dto.endTime(), activity::changeEndTime);
        activityRepository.save(activity);


        // googleCalender update 추가
        eventService.updateEvent(dto,activity.getGoogleEventId(),user.getGoogleCalenderId());
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
        User user = userRepository.findByEmail("asd@example.com").orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
        String googleCalenderId = user.getGoogleCalenderId();
        eventService.deleteEvent(activity.getGoogleEventId(), googleCalenderId);
        activityRepository.deleteById(activityId);
        if (activityRepository.existsById(activityId)) {
            throw ActivityException.ACTIVITY_NOT_FOUND.getException();
        }

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
