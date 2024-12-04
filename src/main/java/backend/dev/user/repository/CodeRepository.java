package backend.dev.user.repository;

import backend.dev.user.entity.AdminCode;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CodeRepository extends JpaRepository<AdminCode, Long> {
    @Query("select a from AdminCode a")
    List<AdminCode> findAllAdminCodes();

    @Query("select a from AdminCode a where a.code = :code")
    boolean checkCode(String code);

    AdminCode findAdminCodeByCode(String code);
}
