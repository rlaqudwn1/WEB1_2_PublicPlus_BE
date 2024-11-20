package backend.dev.movie.repository;

import backend.dev.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query("SELECT m FROM Movie m WHERE m.title LIKE '%in%'")
    List<Movie> findByTitleContaining(String title);
}
