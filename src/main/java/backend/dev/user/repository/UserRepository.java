package backend.dev.user.repository;

import backend.dev.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,String> {
    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("select u from User u join fetch u.oauthList o where o.provider = :provider and o.providerId = :providerId")
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    @Query("select u from User u where u.role = 'USER'")
    List<User> findAllUser();

    @Query("select u from User u where u.role = 'ADMIN'")
    List<User> findAllAdmin();

}
