package backend.dev.facility.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Facility",
        indexes = {
                @Index(name = "facilityCategory", columnList = "category"),
                @Index(name = "area", columnList = "area"),
                @Index(name = "priceType", columnList = "priceType")
        })
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public class Facility extends FacilityBase {


}
