package backend.dev.facility.service;


import backend.dev.facility.entity.Facility;
import backend.dev.facility.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public Facility getFacilityById(int id) {
        return facilityRepository.findById(id).orElse(null);
    }

    public Facility addFacility(Facility facility) {
        return facilityRepository.save(facility);
    }
}
