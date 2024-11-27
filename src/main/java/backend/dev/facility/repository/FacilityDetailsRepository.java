package backend.dev.facility.repository;


import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.repository.querydsl.FacilityRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FacilityDetailsRepository extends JpaRepository<FacilityDetails, String>, FacilityRepositoryCustom {
    Page<FacilityDetails> findByFacilityCategory(FacilityCategory facilityCategory, Pageable pageable);
}
