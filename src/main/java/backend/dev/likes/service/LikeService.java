package backend.dev.likes.service;

import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.exception.FacilityException;
import backend.dev.facility.repository.FacilityDetailsRepository;
import backend.dev.likes.entity.Likes;
import backend.dev.likes.exception.LikeException;
import backend.dev.likes.repository.LikeRepository;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final FacilityDetailsRepository facilityDetailsRepository;

    public void addLike(String facilityId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));
        FacilityDetails facilityDetails = facilityDetailsRepository.findById(facilityId).orElseThrow(FacilityException.FACILITY_NOT_FOUND::getFacilityTaskException);

        if (likeRepository.existsByUserAndFacility(user,facilityDetails)) {
            throw LikeException.DUPLICATE_LIKE.getLikeException();
        }else {
            likeRepository.save(Likes.builder()
                    .user(user)
                    .facility(facilityDetails)
                    .build());
            facilityDetails.changeLikes(facilityDetails.getLikes()+1);
        }
    }
    @Transactional
    public void removeLike(String facilityId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));
        FacilityDetails facilityDetails = facilityDetailsRepository.getReferenceById(facilityId);
        try{

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        likeRepository.deleteByUserAndFacility(user,facilityDetails);
        facilityDetails.changeLikes(facilityDetails.getLikes()-1);
    }
}
