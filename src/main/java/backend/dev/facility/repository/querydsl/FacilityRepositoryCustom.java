package backend.dev.facility.repository.querydsl;

import backend.dev.facility.dto.FacilityFilterDTO;
import backend.dev.facility.entity.FacilityDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacilityRepositoryCustom {
    Page<FacilityDetails> findFacility(FacilityFilterDTO criteria, Pageable pageable);
    Page<FacilityDetails> findFacilityByName(String name);
}

