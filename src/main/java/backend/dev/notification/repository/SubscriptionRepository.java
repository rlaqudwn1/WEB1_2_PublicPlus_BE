package backend.dev.notification.repository;

import backend.dev.notification.entity.TopicSubscription;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubscriptionRepository extends JpaRepository<TopicSubscription, Long> {

}
