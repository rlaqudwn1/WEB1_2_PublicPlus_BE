package backend.dev.googlecalendar.controller;

import backend.dev.activity.dto.ActivityCreateDTO;
import backend.dev.googlecalendar.dto.EventDTO;
import backend.dev.googlecalendar.service.CalenderService;
import backend.dev.googlecalendar.service.EventService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import backend.dev.googlecalendar.setting.GoogleCalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.TimeZone;

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
