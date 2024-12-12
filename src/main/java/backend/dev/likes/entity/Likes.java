package backend.dev.likes.entity;

import backend.dev.facility.entity.FacilityDetails;
import backend.dev.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long like_Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_details_id")
    private FacilityDetails facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id")
    private User user;
}
