package backend.dev.activity.repository;

import backend.dev.activity.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    boolean existsActivityByActivity_IdAndUser_Id(Long activityId, Long userId);

}
