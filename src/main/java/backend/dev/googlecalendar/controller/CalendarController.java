package backend.dev.googlecalendar.controller;

import backend.dev.googlecalendar.service.CalenderService;
import backend.dev.googlecalendar.service.EventService;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import backend.dev.googlecalendar.setting.GoogleCalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CalendarController {
    private final CalenderService calenderService;
    private final EventService eventService;

    private final GoogleCalendarService googleCalendarService;




    @GetMapping("/calendars")
    public ResponseEntity<?> listCalendars() throws IOException {
        Calendar service = googleCalendarService.getCalendarService();
        CalendarList calendarList = service.calendarList().list().execute();

        for (String s : calendarList.keySet()) {
            log.info(s);
        }
        return ResponseEntity.ok(calendarList.getItems());
    }
    @PostMapping("/calendar/{summary}")
    public ResponseEntity<?> createCalendar(@PathVariable String name) throws IOException {

        return ResponseEntity.ok(calenderService.createCalendar(name));
    }

}
