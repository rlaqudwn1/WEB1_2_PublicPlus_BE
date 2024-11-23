package backend.dev.googlecalendar.controller;

import backend.dev.googlecalendar.dto.EventCreatedDTO;
import backend.dev.googlecalendar.dto.EventDTO;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import backend.dev.googlecalendar.setting.GoogleCalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
@RestController
public class CalendarController {

    private final GoogleCalendarService googleCalendarService;

    public CalendarController(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    @GetMapping("/events")
    public Events listEvents() throws IOException {
        Calendar service = googleCalendarService.getCalendarService();
        return service.events().list("primary")
                .setMaxResults(10)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
    }

    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDetails) {
        try {
            Calendar service = googleCalendarService.getCalendarService();

            Event event = new Event()
                    .setSummary(eventDetails.getSummary())
                    .setDescription(eventDetails.getDescription())
                    .setStart(new EventDateTime()
                            .setDateTime(new DateTime(eventDetails.getStart().getDateTime()))
                            .setTimeZone(eventDetails.getStart().getTimeZone()))
                    .setEnd(new EventDateTime()
                            .setDateTime(new DateTime(eventDetails.getEnd().getDateTime()))
                            .setTimeZone(eventDetails.getEnd().getTimeZone()))
                    .setLocation(eventDetails.getLocation());
            Event createdEvent = service.events().insert("primary", event).execute();
            return ResponseEntity.ok(createdEvent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating event: " + e.getMessage());
        }
    }
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
    public ResponseEntity<?> createCalendar(@PathVariable String summary) throws IOException {
        Calendar service = googleCalendarService.getCalendarService();
        com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        calendar.setSummary(summary);
        calendar.setTimeZone("Asia/Seoul");
        return ResponseEntity.ok(service.calendars().insert(calendar).execute());
    }

}
