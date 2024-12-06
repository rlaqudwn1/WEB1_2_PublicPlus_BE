package backend.dev.likes.repository;

import backend.dev.facility.entity.FacilityDetails;
import backend.dev.likes.entity.Likes;
import backend.dev.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes , Long> {
    boolean existsByUserAndFacility(User user, FacilityDetails facility);

    void deleteByUserAndFacility(User user, FacilityDetails facility);
}
