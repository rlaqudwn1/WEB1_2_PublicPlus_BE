package backend.dev.googlecalendar.service;

import backend.dev.googlecalendar.setting.GoogleCalendarService;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
@RequiredArgsConstructor
public class CalenderService {
    private final GoogleCalendarService googleCalendarService;

    public void listCalendars(Calendar service) {
        try {
            CalendarList calendarList = service.calendarList().list().execute();
            for (CalendarListEntry entry : calendarList.getItems()) {
                System.out.println("Calendar ID: " + entry.getId());
                System.out.println("Summary: " + entry.getSummary());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String createCalendar(String name) throws IOException {
        try {
            // Google Calendar 서비스 인스턴스를 가져옵니다.
            Calendar service = googleCalendarService.getCalendarService();

            // 새로운 Calendar 객체를 만들어서 설정합니다.
            com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
            calendar.setSummary(name);  // 캘린더 이름 설정
            calendar.setTimeZone("Asia/Seoul");  // 타임존 설정

            // 캘린더를 Google Calendar API에 삽입합니다.
            com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar).execute();

            // 생성된 캘린더의 ID를 출력합니다.
            System.out.println("생성된 캘린더 ID: " + createdCalendar.getId());
            return createdCalendar.getId();
        } catch (IOException e) {
            // 예외 처리: 에러 메시지를 출력하고, 예외를 던집니다.
            System.err.println("캘린더 생성 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;  // 예외를 다시 던져서 호출자에게 알리기
        }
    }


}
