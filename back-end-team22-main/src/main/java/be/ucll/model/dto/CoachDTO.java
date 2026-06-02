package be.ucll.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CoachDTO {
    private Long id;
    private String bio;
    private String name; // profile.name
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private List<CoachOfferDTO> offers;
    private List<CoachAvailabilityDTO> availability;
    private List<LocationDTO> locations; // important: list of LocationDTO objects

    public CoachDTO(Long id, String bio, String name, Boolean isAvailable, LocalDateTime createdAt,
                    List<CoachOfferDTO> offers, List<CoachAvailabilityDTO> availability, List<LocationDTO> locations) {
        this.id = id;
        this.bio = bio;
        this.name = name;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.offers = offers;
        this.availability = availability;
        this.locations = locations;
    }

    public Long getId() { return id; }
    public String getBio() { return bio; }
    public String getName() { return name; }
    public Boolean getIsAvailable() { return isAvailable; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<CoachOfferDTO> getOffers() { return offers; }
    public List<CoachAvailabilityDTO> getAvailability() { return availability; }
    public List<LocationDTO> getLocations() { return locations; }
}
