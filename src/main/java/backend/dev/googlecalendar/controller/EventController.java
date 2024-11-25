package backend.dev.googlecalendar.controller;

import backend.dev.googlecalendar.setting.GoogleCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/googleCalender")
public class EventController {
    private final GoogleCalendarService googleCalendarService;

//    @GetMapping()
//    public
}
