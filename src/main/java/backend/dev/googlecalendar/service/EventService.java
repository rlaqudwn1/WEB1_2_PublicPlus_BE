package backend.dev.googlecalendar.service;

import backend.dev.activity.dto.ActivityCreateDTO;
import backend.dev.activity.dto.ActivityResponseDTO;
import backend.dev.activity.dto.ActivityUpdateDTO;
import backend.dev.googlecalendar.setting.GoogleCalendarService;
import backend.dev.user.repository.UserRepository;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final GoogleCalendarService googleCalendarService;
    private final UserRepository userRepository;
    private String timeZone = "Asia/Seoul";

    public void getEvent(String eventId) {
        try {
            Calendar service = googleCalendarService.getCalendarService();
            Event event = service.events().get("d053961b19cc4adcd817f8e8603e40704d06a54669cadc3c7ba0134b566064d7@group.calendar.google.com", eventId).execute();
            Event.ExtendedProperties extendedProperties = event.getExtendedProperties();
            Object maxAttendees = extendedProperties.get("maxAttendees");
            System.out.println(extendedProperties.get("maxAttendees"));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ActivityCreateDTO createEvent(ActivityCreateDTO activityCreateDTO) {
        try {
            Calendar service = googleCalendarService.getCalendarService();

            Event event = new Event()
                    .setSummary(activityCreateDTO.getTitle())
                    .setDescription(activityCreateDTO.getDescription())
                    .setStart(new EventDateTime()
                            .setDateTime(new DateTime(activityCreateDTO.getStartTime()))
                            .setTimeZone(timeZone))
                    .setEnd(new EventDateTime()
                            .setDateTime(new DateTime(activityCreateDTO.getEndTime()))
                            .setTimeZone(timeZone))
                    .setLocation(activityCreateDTO.getLocation());
            // Extended Properties에 최대 참석자 수 및 참석자 목록 추가
            event.setExtendedProperties(new Event.ExtendedProperties().setPrivate(
                    Map.of(
                            "maxAttendees", (String.valueOf(activityCreateDTO.getMaxAttendees()))
//                            "attendees", String.join(",", activityCreateDTO.getAttendees())
                    )
            ));
            Event createdEvent = service.events().insert(activityCreateDTO.getGoogleCalenderId(), event).execute();
            activityCreateDTO.setEventId(createdEvent.getId());
            return activityCreateDTO;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public String updateEvent(ActivityUpdateDTO updateDTO) {
        try {
            log.info("업데이트 이벤트 확인: " + updateDTO.getTitle());
            Calendar service = googleCalendarService.getCalendarService();

            // 기존 이벤트 가져오기
            Event event = service.events().get(updateDTO.getGoogleCalenderId(), updateDTO.getEventId()).execute();

            // 이벤트 업데이트
            event.setSummary(updateDTO.getTitle());
            event.setLocation(updateDTO.getLocation());
            event.setDescription(updateDTO.getDescription());
            event.setStart(new EventDateTime()
                    .setDateTime(new DateTime(updateDTO.getStartTime()))
                    .setTimeZone(timeZone));
            event.setEnd(new EventDateTime()
                    .setDateTime(new DateTime(updateDTO.getEndTime()))
                    .setTimeZone(timeZone));

            // 최대 참석 인원수 추가 (extendedProperties)
            Event.ExtendedProperties extendedProperties = event.getExtendedProperties();
            if (extendedProperties == null) {
                extendedProperties = new Event.ExtendedProperties();
            }
            Map<String, String> privateProperties = extendedProperties.getPrivate();
            if (privateProperties == null) {
                privateProperties = new java.util.HashMap<>();
            }
            privateProperties.put("maxAttendees", String.valueOf(updateDTO.getMaxAttendees()));
            extendedProperties.setPrivate(privateProperties);
            event.setExtendedProperties(extendedProperties);

            log.info("Extended properties: " + privateProperties);

            // Google Calendar에 업데이트된 이벤트 저장
            Event updatedEvent = service.events().update(updateDTO.getGoogleCalenderId(), event.getId(), event).execute();

            // 업데이트된 시간 반환
            log.info("이벤트 업데이트 성공: " + updatedEvent.getUpdated());
            Event.ExtendedProperties extendedProperties1 = updatedEvent.getExtendedProperties();
            if (extendedProperties1 != null && extendedProperties1.getPrivate() != null) {
                String maxAttendees = extendedProperties1.getPrivate().get("maxAttendees");
                log.info("maxAttendees: " + maxAttendees);
            } else {
                log.warn("Extended properties or private properties are null");
            }
            return updatedEvent.getUpdated().toString();
        } catch (Exception e) {
            log.error("이벤트 업데이트 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Event update failed", e);
        }
    }


    public String deleteEvent(String eventId, String googleCalendarId) {
        try {
            Calendar service = googleCalendarService.getCalendarService();
            service.events().delete(googleCalendarId,eventId).execute();// 유저 정보에서 캘린더를 받아오고 eventId를 선택
            return "Event deleted";
        }catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public String listEvents(String googleCalendarId) {
        try {
            Calendar calendarService = googleCalendarService.getCalendarService();
            Events events = calendarService.events().list(googleCalendarId).execute();
            List<Event> items = events.getItems();
            for (Event event : items) {
                System.out.println(event.getSummary());
            }
            return "Str";
        }catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
