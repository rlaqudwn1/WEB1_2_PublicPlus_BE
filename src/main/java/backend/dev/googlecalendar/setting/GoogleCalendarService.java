package backend.dev.googlecalendar.setting;

import backend.dev.googlecalendar.dto.EventDTO;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

@Component
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "Google Calendar API Integration";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials.json";

    public Calendar getCalendarService() throws IOException {
        // HTTP Transport
        final NetHttpTransport httpTransport = new NetHttpTransport();

        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, new FileReader(CREDENTIALS_FILE_PATH)
        );

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, Collections.singletonList(CalendarScopes.CALENDAR)
        ).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setHost("localhost")
                .setPort(8081) // 고정된 포트를 사용
                .build();

        // Authorize user
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
//    public void createEvent(EventDTO eventDTO){
//        Event event = new Event()
//                .setSummary(eventDTO.getTitle())
//                .setLocation(eventDTO.getLocation())
//                .setStart(new EventDateTime().setDateTime(eventDTO.getStart()).setTimeZone("Asia/Seoul"))
//                .setEnd(new EventDateTime().setDateTime(eventDTO.getStart()).setTimeZone("Asia/Seoul"));
//    }


}
