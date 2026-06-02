// be/ucll/model/LocationPartnership.java
package be.ucll.model;

import jakarta.persistence.*;

@Entity
@Table(name = "LOCATION_PARTNERSHIPS")
public class LocationPartnership {

    @EmbeddedId
    private LocationPartnershipId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("locationId")
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private PartnershipCategory category;

    @Column(nullable = false)
    private boolean decided = false;

    public LocationPartnership() {}

    public LocationPartnership(Location location, PartnershipCategory category, boolean decided) {
        this.location = location;
        this.category = category;
        this.decided = decided;
        this.id = new LocationPartnershipId(location.getBenchId(), category.getId());
    }

    public LocationPartnershipId getId() { return id; }
    public Location getLocation() { return location; }
    public PartnershipCategory getCategory() { return category; }
    public boolean isDecided() { return decided; }

    public void setId(LocationPartnershipId id) { this.id = id; }
    public void setLocation(Location location) { this.location = location; }
    public void setCategory(PartnershipCategory category) { this.category = category; }
    public void setDecided(boolean decided) { this.decided = decided; }
}
