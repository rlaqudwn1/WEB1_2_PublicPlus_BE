package backend.dev.activity.dto;

import backend.dev.activity.entity.Activity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityCreateDTO
 {
     private String title;
     private String eventId;// 구글 캘린더 EventId
     private String description;
     private String location;
     private String startTime;
     private String endTime;
     private String googleCalenderId;
     private int maxAttendees;
 }
