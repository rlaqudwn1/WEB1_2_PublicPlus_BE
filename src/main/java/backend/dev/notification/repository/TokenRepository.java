package backend.dev.notification.repository;

import backend.dev.notification.entity.FCMToken;
import backend.dev.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<FCMToken, Long> {
    Optional<FCMToken> findByUser(User user);
}
