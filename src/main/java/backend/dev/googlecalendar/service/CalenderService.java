package backend.dev.googlecalendar.service;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;

import java.io.IOException;

public class CalenderService {
    public void listCalendars(Calendar service) {
        try {
            CalendarList calendarList = service.calendarList().list().execute();
            for (CalendarListEntry entry : calendarList.getItems()) {
                System.out.println("Calendar ID: " + entry.getId());
                System.out.println("Summary: " + entry.getSummary());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
