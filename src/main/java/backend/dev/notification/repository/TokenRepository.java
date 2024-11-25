package backend.dev.notification.repository;

import backend.dev.notification.entity.FCMToken;
import backend.dev.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<FCMToken, Long> {
    FCMToken findByUser(User user);
}
