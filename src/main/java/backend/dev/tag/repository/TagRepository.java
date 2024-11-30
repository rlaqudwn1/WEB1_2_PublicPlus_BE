package backend.dev.tag.repository;

import backend.dev.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByReviewReviewId(Long reviewId);
}