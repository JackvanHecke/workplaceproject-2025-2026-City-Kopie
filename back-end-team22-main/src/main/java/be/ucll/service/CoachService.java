package be.ucll.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import be.ucll.model.Coach;
import be.ucll.model.CoachAvailability;
import be.ucll.model.CoachOffer;
import be.ucll.model.Location;
import be.ucll.model.dto.CoachAvailabilityDTO;
import be.ucll.model.dto.CoachDTO;
import be.ucll.model.dto.CoachOfferDTO;
import be.ucll.model.dto.LocationDTO;
import be.ucll.repository.CoachRepository;

@Service
public class CoachService {

    private final CoachRepository coachRepository;

    public CoachService(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    public List<CoachDTO> getAllCoaches() {
        return coachRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CoachDTO getCoachById(Long id) {
        Coach coach = coachRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coach not found with id: " + id));
        return toDTO(coach);
    }

    public CoachDTO toDTO(Coach coach) {

        Set<Location> locEntities =
                coach.getLocations() == null ? Collections.emptySet() : coach.getLocations();

        Set<CoachOffer> offerEntities =
                coach.getOffers() == null ? Collections.emptySet() : coach.getOffers();

        Set<CoachAvailability> availEntities =
                coach.getAvailability() == null ? Collections.emptySet() : coach.getAvailability();

        // --- LOCATION MAPPING (matches your current 22-arg LocationDTO ctor) ---
        List<LocationDTO> locations = locEntities.stream()
                .map(this::toLocationDTO)
                .collect(Collectors.toList());

        // --- OFFERS MAPPING ---
        List<CoachOfferDTO> offers = offerEntities.stream()
                .map(o -> new CoachOfferDTO(
                        o.getId(),
                        o.getOfferType(),
                        o.getTargetGroup(),
                        o.getDescription(),
                        o.getFreeOrPaid() != null ? o.getFreeOrPaid().name() : null,
                        o.getPrice(),
                        o.getRecurrence(),
                        o.getStartDatetime(),
                        o.getEndDatetime(),
                        o.getCreatedAt()
                ))
                .collect(Collectors.toList());

        // --- AVAILABILITY MAPPING (bench → LocationDTO) ---
        List<CoachAvailabilityDTO> availability = availEntities.stream()
                .map(a -> {
                    Location bench = a.getBench();
                    LocationDTO benchDto = (bench != null) ? toLocationDTO(bench) : null;

                    return new CoachAvailabilityDTO(
                            a.getId(),
                            a.getAvailableFrom(),
                            a.getAvailableTo(),
                            a.getNote(),
                            a.getIsRecurring(),
                            a.getRecurrenceRule(),
                            benchDto,
                            a.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());

        String profileName =
                coach.getProfile() != null ? coach.getProfile().getName() : null;

        return new CoachDTO(
                coach.getId(),
                coach.getBio(),
                profileName,
                coach.getIsAvailable(),
                coach.getCreatedAt(),
                offers,
                availability,
                locations
        );
    }

    private LocationDTO toLocationDTO(Location loc) {
        Long routeId = null;
        String routeName = null;
        String routeType = null;

        if (loc.getMovementRoute() != null) {
            routeId = loc.getMovementRoute().getId();
            routeName = loc.getMovementRoute().getName();
            routeType = loc.getMovementRoute().getType();
        }

        // NOTE: Use the same null-safe approach as in LocationService if needed.
        Integer connectedRoutes = null;
        // If loc.getConnectedRoutes() returns Integer, keep as-is:
        // connectedRoutes = loc.getConnectedRoutes();
        // If it returns int, wrap it:
        connectedRoutes = loc.getConnectedRoutes();

        return new LocationDTO(
                loc.getBenchId(),
                loc.getBenchName(),
                loc.getBenchOwner(),
                loc.getBenchStreet(),
                loc.getBenchHouseNumber(),
                loc.getBenchPostalCode(),
                loc.getBenchCity(),
                loc.getBenchCountry(),
                loc.getBenchSize(),
                loc.getBenchType(),
                connectedRoutes,
                loc.getTags(),
                loc.getShowInApp(),
                loc.getMobile(),
                loc.getPublicAvailable(),
                loc.getLatitude(),
                loc.getLongitude(),
                loc.getStations(),
                loc.getPhotoUrl(),
                routeId,
                routeName,
                routeType,
                loc.getCompletedChecklistItems()
        );
    }
}
