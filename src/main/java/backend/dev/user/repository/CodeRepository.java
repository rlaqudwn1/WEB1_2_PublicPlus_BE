package backend.dev.user.repository;

import backend.dev.user.entity.AdminCode;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<AdminCode,Long> {

    List<AdminCode> findAllAdminCodes();

    boolean checkCode(String code);
    AdminCode findAdminCodeByCode(String code);
}
