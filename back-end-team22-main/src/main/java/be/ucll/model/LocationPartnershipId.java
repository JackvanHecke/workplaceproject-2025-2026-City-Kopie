// be/ucll/model/LocationPartnershipId.java
package be.ucll.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LocationPartnershipId implements Serializable {

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "category_id")
    private Long categoryId;

    public LocationPartnershipId() {}

    public LocationPartnershipId(Long locationId, Long categoryId) {
        this.locationId = locationId;
        this.categoryId = categoryId;
    }

    public Long getLocationId() { return locationId; }
    public Long getCategoryId() { return categoryId; }

    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationPartnershipId that)) return false;
        return Objects.equals(locationId, that.locationId) &&
                Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, categoryId);
    }
}
