package be.ucll.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COACHES")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // one-to-one to existing Profile
    @OneToOne(optional = false)
    @JoinColumn(name = "profile_id", unique = true, nullable = false)
    private Profile profile;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "notify_new_devices")
    private Boolean notifyNewDevices = Boolean.TRUE;

    @Column(name = "default_radius_km")
    private Integer defaultRadiusKm = 5;

    @Column(name = "is_available")
    private Boolean isAvailable = Boolean.TRUE;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // M:N with Location via join table COACH_LOCATIONS
    @ManyToMany
    @JoinTable(
            name = "COACH_LOCATIONS",
            joinColumns = @JoinColumn(name = "coach_id"),
            inverseJoinColumns = @JoinColumn(name = "bench_id")
    )
    private Set<Location> locations = new HashSet<>();

    // offers and availability: one-to-many relationships (normalized)
    @OneToMany(mappedBy = "coach")
    private Set<CoachOffer> offers = new HashSet<>();

    @OneToMany(mappedBy = "coach")
    private Set<CoachAvailability> availability = new HashSet<>();

    public Coach() {}

    public Coach(Profile profile) { this.profile = profile; }

    // getters and setters ...

    public Long getId() { return id; }
    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public Boolean getNotifyNewDevices() { return notifyNewDevices; }
    public void setNotifyNewDevices(Boolean notifyNewDevices) { this.notifyNewDevices = notifyNewDevices; }
    public Integer getDefaultRadiusKm() { return defaultRadiusKm; }
    public void setDefaultRadiusKm(Integer defaultRadiusKm) { this.defaultRadiusKm = defaultRadiusKm; }
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Set<Location> getLocations() { return locations; }
    public void setLocations(Set<Location> locations) { this.locations = locations; }
    public Set<CoachOffer> getOffers() { return offers; }
    public void setOffers(Set<CoachOffer> offers) { this.offers = offers; }
    public Set<CoachAvailability> getAvailability() { return availability; }
    public void setAvailability(Set<CoachAvailability> availability) { this.availability = availability; }

    // convenience helpers
    public void addLocation(Location location) {
        locations.add(location);
    }
    public void removeLocation(Location location) {
        locations.remove(location);
    }
    public void addOffer(CoachOffer offer) {
        offers.add(offer);
        offer.setCoach(this);
    }
    public void removeOffer(CoachOffer offer) {
        offers.remove(offer);
        offer.setCoach(null);
    }
    public void addAvailability(CoachAvailability slot) {
        availability.add(slot);
        slot.setCoach(this);
    }
    public void removeAvailability(CoachAvailability slot) {
        availability.remove(slot);
        slot.setCoach(null);
    }
}
