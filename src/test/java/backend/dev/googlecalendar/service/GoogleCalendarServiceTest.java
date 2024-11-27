//package backend.dev.googlecalendar.service;
//
//import backend.dev.googlecalendar.setting.GoogleCalendarService;
//import com.google.api.services.calendar.Calendar;
//import com.google.api.services.calendar.model.Event;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//
//@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
//public class GoogleCalendarServiceTest {
//    @Autowired
//    private GoogleCalendarService googleCalendarService;
//
//    @Test
//    @DisplayName("구글 캘린더 API 테스트 입니다")
//    void TempateTest(){
//        //given
//
//        //when
//
//        //then
//    }
//    @Test
//    @DisplayName("구글 캘린더 GET API 테스트")
//    public void getEvent() {
//        try {
//            String eventId = "";
//            Calendar service = googleCalendarService.getCalendarService();
//            Event event = service.events().get("d053961b19cc4adcd817f8e8603e40704d06a54669cadc3c7ba0134b566064d7@group.calendar.google.com", eventId).execute();
//            Event.ExtendedProperties extendedProperties = event.getExtendedProperties();
//            Object maxAttendees = extendedProperties.get("maxAttendees");
//            System.out.println(extendedProperties.get("maxAttendees"));
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
