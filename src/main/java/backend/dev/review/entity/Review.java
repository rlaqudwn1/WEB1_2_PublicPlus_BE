package backend.dev.review.entity;

import backend.dev.facility.entity.FacilityDetails;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long review_id;

    @ManyToOne
    @JoinColumn(name = "facility_id",  nullable = false)
    private FacilityDetails facilityDetails;

    private String review_content;
    private Double review_rating;

    @Column(name = "review_likes")
    private Integer reviewLikes;

    @Column(name = "review_views")
    private Integer reviewViews;

    private LocalDateTime review_createdAt;
    private LocalDateTime review_updatedAt;

    @PrePersist
    public void prePersist() {
        this.review_createdAt = LocalDateTime.now();
        this.review_updatedAt = LocalDateTime.now();
        this.reviewLikes = 0;
        this.reviewViews = 0;
    }

    @PreUpdate
    public void preUpdate() {
        this.review_updatedAt = LocalDateTime.now();
    }
}