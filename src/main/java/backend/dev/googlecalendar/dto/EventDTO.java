package backend.dev.googlecalendar.dto;

import com.google.api.client.util.DateTime;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class EventDTO {
    private String summary;
    private String description;
    private EventDateTime start;
    private EventDateTime end;
    private String location;

    // Getters and Setters
    @Data
    public static class EventDateTime {
        private String dateTime;
        private String timeZone;

        // Getters and Setters
    }
}