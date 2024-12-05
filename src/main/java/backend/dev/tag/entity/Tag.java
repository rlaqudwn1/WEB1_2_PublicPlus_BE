package backend.dev.tag.entity;

import backend.dev.review.entity.Review;
import backend.dev.tag.enums.TagValue;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagValue tagValue;

    public Tag(Review review, TagValue tagValue) {
        this.review = review;
        this.tagValue = tagValue;
    }
    public TagValue getTagValue() {
        return this.tagValue;
    }

}