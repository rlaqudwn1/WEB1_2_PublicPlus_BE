package backend.dev.googlecalendar.controller;

import backend.dev.activity.dto.ActivityRequestDTO;
import backend.dev.activity.dto.ActivityResponseDTO;
import backend.dev.googlecalendar.service.EventService;
import backend.dev.googlecalendar.setting.GoogleCalendarService;
import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.entity.User;
import backend.dev.user.service.UserService;
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
    private final UserService userService;
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
    public ResponseEntity<?> createEvent(@RequestBody ActivityRequestDTO dto, String email ) {
        User user = userService.findUserByEmail(email).orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));
        return ResponseEntity.ok(eventService.createEvent(dto,user.getEmail()));
    }
    @PutMapping("")
    public ResponseEntity<?> updateEvent(@RequestBody ActivityRequestDTO dto,String email) throws IOException {
        User user = userService.findUserByEmail(email).orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));
        return ResponseEntity.ok(eventService.updateEvent(dto,email,user.getEmail()));
    }

    @DeleteMapping("/eventId")
    public ResponseEntity<String> deleteEvent(@RequestBody String eventId, String googleCalenderId) {
        return ResponseEntity.ok(eventService.deleteEvent(eventId,googleCalenderId));
    }

//    @GetMapping()
//    public
}
