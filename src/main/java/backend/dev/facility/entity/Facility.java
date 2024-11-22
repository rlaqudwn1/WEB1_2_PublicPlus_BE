package backend.dev.facility.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "facility",
        indexes = {
//                @Index(name = "facilityCategory", columnList = "category"),
//                @Index(name = "area", columnList = "area"),
//                @Index(name = "priceType", columnList = "priceType")
        })
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@ToString

public class Facility extends FacilityBase {

        public Facility() {

        }

        public void setFacilityId(String l) {
        }
}
