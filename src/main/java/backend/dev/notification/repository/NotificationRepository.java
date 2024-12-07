package backend.dev.notification.repository;

import backend.dev.notification.entity.Notification;
import backend.dev.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    public List<Notification> findByUser(User user);
}
