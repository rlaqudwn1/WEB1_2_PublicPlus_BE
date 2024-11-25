package backend.dev.googlecalendar.dto;

import lombok.Data;

@Data
public class EventCreatedDTO {
    private String summary;
    private String description;
    private EventDateTime start;
    private EventDateTime end;

    // Getters and Setters

    public static class EventDateTime {
        private String dateTime;
        private String timeZone;

        // Getters and Setters
    }
}