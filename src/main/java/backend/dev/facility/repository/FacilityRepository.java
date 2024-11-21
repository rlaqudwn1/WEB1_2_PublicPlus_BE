package backend.dev.facility.repository;


import backend.dev.facility.entity.Facility;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.repository.querydsl.FacilityRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FacilityRepository extends JpaRepository<Facility, String>, FacilityRepositoryCustom {
    Page<Facility> findByFacilityCategory(FacilityCategory facilityCategory, Pageable pageable);
}
