package backend.dev.facility.repository.querydsl;

import backend.dev.facility.dto.FacilityFilterDTO;
import backend.dev.facility.entity.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacilityRepositoryCustom {
    Page<Facility> findFacility(FacilityFilterDTO criteria, Pageable pageable);
    Page<Facility> findFacilityByName(String name);
}

