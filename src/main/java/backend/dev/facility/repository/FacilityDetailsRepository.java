package backend.dev.facility.repository;


import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.repository.querydsl.FacilityRepositoryCustom;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityDetailsRepository extends JpaRepository<FacilityDetails, String>, FacilityRepositoryCustom {
    Page<FacilityDetails> findByFacilityCategory(FacilityCategory facilityCategory, Pageable pageable);

    @Transactional
    @Modifying
    default int incrementViews(String facilityId) {
        return findById(facilityId).map(facility -> {
            facility.changeViews(facility.getViews() + 1);
            save(facility);
            return 1;
        }).orElse(0);
    }
}
