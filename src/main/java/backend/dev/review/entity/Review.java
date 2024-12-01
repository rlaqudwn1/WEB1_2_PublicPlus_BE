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
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = false)
    private FacilityDetails facility;

    private String review_content;
    private Double review_rating;

    @Column(name = "review_likes")
    private Integer reviewLikes;

    @Column(name = "review_createdAt")
    private LocalDateTime createdAt;

    @Column(name = "review_updatedAt")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.reviewLikes = 0;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}