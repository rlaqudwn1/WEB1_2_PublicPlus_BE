package backend.dev.user.repository;

import backend.dev.user.entity.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthRepository extends JpaRepository<Oauth,Long> {
}
