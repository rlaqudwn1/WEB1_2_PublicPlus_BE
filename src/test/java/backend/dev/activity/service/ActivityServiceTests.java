//package backend.dev.activity.service;
//
//import backend.dev.activity.dto.ActivityUpdateDTO;
//import backend.dev.activity.entity.Activity;
//import backend.dev.activity.exception.ActivityException;
//import backend.dev.activity.repository.ActivityRepository;
////import backend.dev.testdata.ActivityInitializer;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.TestPropertySource;
//
//@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
////@Import(ActivityInitializer.class) // FacilityInitializer를 테스트 클래스에 임포트
//public class ActivityServiceTests {
//    @Autowired
//    private ActivityRepository activityRepository;
//
//    @Test
//    @DisplayName("모임 업데이트 테스트입니다")
//    void ActivityUpdateTest(){
//        //given 알람 업데이트 DTO가 주어졌을 경우
//        Long activityId = 1L;
//        ActivityUpdateDTO updateDTO = ActivityUpdateDTO.builder()
//                .description("업데이트 설명")
//                .title("업데이트 중")
//                .build();
//        //when
//        Activity activity = activityRepository.findById(activityId).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
//        activity.changeDescription(updateDTO.getDescription());
//        activity.changeTitle(updateDTO.getTitle());
//
//        //then
//        Assertions.assertEquals(updateDTO.getDescription(), activity.getDescription(),"설명이 업데이트 중이라고 바뀌어야 합니다");
//        Assertions.assertEquals(updateDTO.getTitle(), activity.getTitle(),"제목이 업데이트 설명으로 바뀌어야 합니다");
//    }
//    @Test
//    @DisplayName("모임 삭제 테스트")
//    void ActivityDeleteTest(){
//        //given 삭제할 아이디
//        Long activityId = 2L;
//        Activity activity = activityRepository.findById(activityId).orElseThrow(ActivityException.ACTIVITY_NOT_FOUND::getException);
//        //when activity를 삭제 할 경우
//        activityRepository.delete(activity);
//        //then 모임이 삭제되었는지 테스트
//        Assertions.assertFalse(activityRepository.existsById(activityId));
//    }
//}
