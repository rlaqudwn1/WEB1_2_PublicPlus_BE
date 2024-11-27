package backend.dev.googlecalendar.controller;

import backend.dev.activity.dto.ActivityCreateDTO;
import backend.dev.activity.dto.ActivityUpdateDTO;
import backend.dev.googlecalendar.service.CalenderService;
import backend.dev.googlecalendar.service.EventService;
import backend.dev.googlecalendar.setting.GoogleCalendarService;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google-calendar/event")
public class EventController {
    private final GoogleCalendarService googleCalendarService;
    private final EventService eventService;
    @GetMapping("/events")
    public Events listEvents() throws IOException {
        Calendar service = googleCalendarService.getCalendarService();
        return service.events().list("primary")
                .setMaxResults(10)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
    }
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody ActivityCreateDTO activityCreateDTO) {

        return ResponseEntity.ok(eventService.createEvent(activityCreateDTO));
    }
    @PutMapping("")
    public ResponseEntity<?> updateEvent(@RequestBody ActivityUpdateDTO activityUpdateDTO) {
        return ResponseEntity.ok(eventService.updateEvent(activityUpdateDTO));
    }

    @DeleteMapping("/eventId")
    public ResponseEntity<String> deleteEvent(@RequestBody String eventId) {
        return ResponseEntity.ok(eventService.deleteEvent(eventId));
    }

//    @GetMapping()
//    public
}
