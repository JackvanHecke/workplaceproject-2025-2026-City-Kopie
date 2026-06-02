package be.ucll.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "profile_location_follow")
public class ProfileFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // wie volgt
    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    // welke bank
    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id")
    private Location location;

    // radius in km rond deze bank
    private int radiusKm;

    // laatste keer dat we voor deze combinatie gecheckt hebben
    private Instant lastChecked;

    public ProfileFollow() {}

    public ProfileFollow(Profile profile, Location location, int radiusKm, Instant lastChecked) {
        this.profile = profile;
        this.location = location;
        this.radiusKm = radiusKm;
        this.lastChecked = lastChecked;
    }

    public Long getId() { return id; }

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public int getRadiusKm() { return radiusKm; }
    public void setRadiusKm(int radiusKm) { this.radiusKm = radiusKm; }

    public Instant getLastChecked() { return lastChecked; }
    public void setLastChecked(Instant lastChecked) { this.lastChecked = lastChecked; }
}
