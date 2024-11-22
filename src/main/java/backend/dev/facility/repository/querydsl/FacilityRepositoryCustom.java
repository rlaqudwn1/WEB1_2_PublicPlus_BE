package backend.dev.facility.repository.querydsl;

import backend.dev.facility.dto.FacilitySearchCriteriaDTO;
import backend.dev.facility.entity.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface FacilityRepositoryCustom {
    Page<Facility> findFacility(FacilitySearchCriteriaDTO criteria, Pageable pageable);
    Page<Facility> findFacilityByName(String name);
}

