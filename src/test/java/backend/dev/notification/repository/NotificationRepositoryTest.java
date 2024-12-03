package backend.dev.notification.repository;

import backend.dev.testdata.FacilityInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application-test.properties")
@Import(FacilityInitializer.class) // FacilityInitializer를 테스트 클래스에 임포트
public class NotificationRepositoryTest {
}
