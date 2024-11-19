package backend.dev.user.repository;

import backend.dev.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {

    User findByEmail(String email);
}
