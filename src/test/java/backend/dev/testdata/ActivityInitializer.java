//package backend.dev.testdata;
//
//import backend.dev.activity.entity.Activity;
//import backend.dev.activity.entity.Participant;
//import backend.dev.activity.repository.ActivityRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Component
//public class ActivityInitializer {
//
//    @Autowired
//    private ActivityRepository activityRepository; // 실제 repository를 주입받습니다.
//
//    @PostConstruct
//    public void initTestData() {
//        try {
//            for (int i = 0; i < 50; i++) {
//                Activity activity = createTestActivity(i);
//                activityRepository.save(activity);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to initialize activity data: " + e.getMessage(), e);
//        }
//    }
//
//
//    private Activity createTestActivity(int index) {
//        Random random = new Random();
//
//        // 임의의 데이터를 생성
//        String[] locations = new String[] {"Seoul", "Daegu", "Busan", "Incheon", "Ulsan"};
//        String[] titles = new String[] {"Morning Yoga", "Evening Run", "Cooking Class", "Football Match", "Cycling Tour"};
//        String[] descriptions = new String[] {
//                "A relaxing morning yoga session",
//                "Join us for an energetic evening run",
//                "Learn how to cook delicious dishes",
//                "Join a friendly football match",
//                "Take a cycling tour around the city"
//        };
//
//        String location = locations[random.nextInt(locations.length)];
//        String title = titles[random.nextInt(titles.length)];
//        String description = descriptions[random.nextInt(descriptions.length)];
//        LocalDateTime startTime = LocalDateTime.now().plusHours(random.nextInt(24)); // 현재 시간부터 24시간 이내
//        LocalDateTime endTime = startTime.plusHours(random.nextInt(2) + 1); // 시작 시간 이후 1~2시간 이내
//        int maxParticipants = random.nextInt(50) + 10; // 10명에서 60명 사이
//        int currentParticipants = random.nextInt(maxParticipants + 1); // 현재 참석자는 최대 참석자 수 이하
//        String googleEventId = "google-event-id-" + index;
//
//
//        return Activity.builder()
//                .title(title)
//                .description(description)
//                .location(location)
//                .startTime(startTime)
//                .endTime(endTime)
//                .maxParticipants(maxParticipants)
//                .currentParticipants(currentParticipants)
//                .googleEventId(googleEventId)
//                .build();
//    }
//
//}
