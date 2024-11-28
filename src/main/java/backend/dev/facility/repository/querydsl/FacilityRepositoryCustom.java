package backend.dev.facility.repository.querydsl;

import backend.dev.facility.dto.FacilityFilterDTO;
import backend.dev.facility.dto.facility.FacilityResponseDTO;
import backend.dev.facility.entity.FacilityDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FacilityRepositoryCustom {
    Page<FacilityDetails> findFacility(FacilityFilterDTO criteria, Pageable pageable);
    Page<FacilityDetails> findFacilityByName(String name);


    Page<FacilityDetails> findFacilitiesByLocation(Double latitude, Double longitude, Double radius, Pageable pageable);
}

