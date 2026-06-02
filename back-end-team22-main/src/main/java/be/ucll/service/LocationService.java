package be.ucll.service;

import be.ucll.model.Location;
import be.ucll.model.dto.LocationDTO;
import be.ucll.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationDTO> getAllLocationDTOs() {
        return locationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<LocationDTO> getMineLocationDTOs(String owner) {
        return locationRepository.findByBenchOwner(owner)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

        public List<String> findAllNames() {
        return locationRepository.findAllNames();
    }

    public Optional<LocationDTO> getLocationDTOById(Long id) {
        return locationRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Location getEntityById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }

    @Transactional
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Transactional
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    @Transactional
    public Location updateLocation(Long id, LocationDTO dto) {
        Location loc = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));

        if (dto.getName() != null) loc.setBenchName(dto.getName());
        if (dto.getCity() != null) loc.setBenchCity(dto.getCity());
        if (dto.getCountry() != null) loc.setBenchCountry(dto.getCountry());
        if (dto.getOwner() != null) loc.setBenchOwner(dto.getOwner());

        if (dto.getStreet() != null) loc.setBenchStreet(dto.getStreet());
        if (dto.getHouseNumber() != null) loc.setBenchHouseNumber(dto.getHouseNumber());
        if (dto.getZipCode() != null) loc.setBenchPostalCode(dto.getZipCode());

        if (dto.getSize() != null) loc.setBenchSize(dto.getSize());
        if (dto.getType() != null) loc.setBenchType(dto.getType());

        if (dto.getTags() != null) loc.setTags(dto.getTags());
        if (dto.getStations() != null) loc.setStations(dto.getStations());

        if (dto.getShowInApp() != null) loc.setShowInApp(dto.getShowInApp());
        if (dto.getMobile() != null) loc.setMobile(dto.getMobile());
        if (dto.getPublicAvailable() != null) loc.setPublicAvailable(dto.getPublicAvailable());

        if (dto.getLatitude() != null) loc.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) loc.setLongitude(dto.getLongitude());

        if (dto.getPhotoUrl() != null) loc.setPhotoUrl(dto.getPhotoUrl());

        return locationRepository.save(loc);
    }

    @Transactional
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new RuntimeException("Location not found with id: " + id);
        }
        locationRepository.deleteById(id);
    }

    private LocationDTO mapToDTO(Location loc) {
        Long routeId = null;
        String routeName = null;
        String routeType = null;

        if (loc.getMovementRoute() != null) {
            routeId = loc.getMovementRoute().getId();
            routeName = loc.getMovementRoute().getName();
            routeType = loc.getMovementRoute().getType();
        }

        return new LocationDTO(
                loc.getBenchId(),
                nonNull(loc.getBenchName()),
                nonNull(loc.getBenchOwner()),
                nonNull(loc.getBenchStreet()),
                nonNull(loc.getBenchHouseNumber()),
                nonNull(loc.getBenchPostalCode()),
                nonNull(loc.getBenchCity()),
                nonNull(loc.getBenchCountry()),
                nonNull(loc.getBenchSize()),
                nonNull(loc.getBenchType()),
                loc.getConnectedRoutes(),
                nonNull(loc.getTags()),

                // flags
                loc.getShowInApp() != null ? loc.getShowInApp() : false,
                loc.getMobile() != null ? loc.getMobile() : false,
                loc.getPublicAvailable() != null ? loc.getPublicAvailable() : false,

                // coords
                loc.getLatitude(),
                loc.getLongitude(),

                // stations + photo
                nonNull(loc.getStations()),
                nonNull(loc.getPhotoUrl()),

                // route info
                routeId,
                routeName,
                routeType,
                loc.getCompletedChecklistItems()
        );
    }

    /* ===================== COUNTERS ===================== */

    public long countAllLocations() {
        return locationRepository.count();
    }

    public long countDistinctCountries() {
        return locationRepository.countDistinctCountries();
    }

    /* ===================== NEARBY SEARCH ===================== */

    public static class NearbyLocation {
        private final Location location;
        private final double distanceKm;

        public NearbyLocation(Location location, double distanceKm) {
            this.location = location;
            this.distanceKm = distanceKm;
        }

        public Location getLocation() {
            return location;
        }

        public double getDistanceKm() {
            return distanceKm;
        }
    }

    public List<NearbyLocation> findNewLocationsWithin(double centerLat,
                                                       double centerLon,
                                                       int radiusKm,
                                                       Instant since) {
        return locationRepository.findAll()
                .stream()
                .filter(loc -> loc.getLatitude() != null && loc.getLongitude() != null)
                .filter(loc -> loc.getCreatedAt() != null && !loc.getCreatedAt().isBefore(since))
                .map(loc -> new NearbyLocation(
                        loc,
                        distanceInKm(centerLat, centerLon, loc.getLatitude(), loc.getLongitude())
                ))
                .filter(n -> n.getDistanceKm() <= radiusKm)
                .sorted(Comparator.comparingDouble(NearbyLocation::getDistanceKm))
                .collect(Collectors.toList());
    }

    private double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(lat1)) *
                                Math.cos(Math.toRadians(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }


    private String nonNull(String s) {
        return s == null ? "" : s;
    }
}
