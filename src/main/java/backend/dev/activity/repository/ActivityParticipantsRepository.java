package backend.dev.activity.repository;

import backend.dev.activity.entity.Activity;
import backend.dev.activity.entity.ActivityParticipants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ActivityParticipantsRepository extends JpaRepository<ActivityParticipants, Long> {
    @Query("SELECT ap.activity FROM ActivityParticipants ap WHERE ap.user.userId = :userId")
    Page<Activity> findActivitiesByUserId(@Param("userId") String userId, Pageable pageable);
    void deleteByUserId(@Param("userId") String userId);
    boolean existsByActivityAndUserId(@Param("activity") Activity activity,@Param("userId") String userId);
}
