package be.ucll.controller;

import be.ucll.model.Location;
import be.ucll.model.dto.LocationDTO;
import be.ucll.model.dto.LocationPartnershipDTO;
import be.ucll.model.dto.StakeholderDTO;
import be.ucll.repository.LocationRepository;
import be.ucll.service.LocationPartnershipService;
import be.ucll.service.LocationService;
import be.ucll.service.StakeholderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/locations")
@CrossOrigin(origins = "http://localhost:8000")
public class LocationRestController {

    private final LocationService locationService;
    private final LocationRepository locationRepository;
    private final LocationPartnershipService locationPartnershipService;
    private final StakeholderService stakeholderService;

    public LocationRestController(
            LocationService locationService,
            LocationRepository locationRepository,
            LocationPartnershipService locationPartnershipService,
            StakeholderService stakeholderService
    ) {
        this.locationService = locationService;
        this.locationRepository = locationRepository;
        this.locationPartnershipService = locationPartnershipService;
        this.stakeholderService = stakeholderService;
    }

    @GetMapping
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocationDTOs();
    }

    @GetMapping("/counters")
    public Map<String, Long> getCounters() {
        return Map.of(
                "total_benches", locationService.countAllLocations(),
                "distinct_countries", locationService.countDistinctCountries()
        );
    }

    @GetMapping("/{id}")
    public LocationDTO getLocationById(@PathVariable Long id) {
        return locationService.getLocationDTOById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody Location location) {
        Location created = locationService.createLocation(location);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(locationService.getLocationDTOById(created.getBenchId()).orElseThrow());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable Long id, @RequestBody LocationDTO dto) {
        Location updated = locationService.updateLocation(id, dto);
        return ResponseEntity.ok(locationService.getLocationDTOById(updated.getBenchId()).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public List<LocationDTO> getMine(@RequestParam String owner) {
        return locationService.getMineLocationDTOs(owner);
    }

    @PostMapping(
            value = "/{id}/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LocationDTO> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Location location = locationService.getEntityById(id);

            Path uploadDir = Paths.get("uploads/benches");
            Files.createDirectories(uploadDir);

            String safeOriginal = (file.getOriginalFilename() == null) ? "upload.jpg" : file.getOriginalFilename();
            String filename = "bench-" + id + "-" + safeOriginal.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path target = uploadDir.resolve(filename);

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            String url = "/uploads/benches/" + filename;
            location.setPhotoUrl(url);

            Location saved = locationService.save(location);

            LocationDTO dto = locationService.getLocationDTOById(saved.getBenchId()).orElseThrow();
            return ResponseEntity.ok(dto);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/partnerships")
    public List<LocationPartnershipDTO> getPartnershipsForLocation(@PathVariable Long id) {
        return locationPartnershipService.getForLocation(id);
    }

    @PutMapping("/{id}/partnerships/{categoryId}/toggle")
    public LocationPartnershipDTO togglePartnership(
            @PathVariable Long id,
            @PathVariable Long categoryId
    ) {
        return locationPartnershipService.toggle(id, categoryId);
    }

    @PutMapping("/{id}/partnerships/{categoryId}")
    public LocationPartnershipDTO setPartnershipDecided(
            @PathVariable Long id,
            @PathVariable Long categoryId,
            @RequestParam boolean decided
    ) {
        return locationPartnershipService.setDecided(id, categoryId, decided);
    }

    @GetMapping("/{id}/stakeholders")
    public List<StakeholderDTO> getStakeholdersForLocation(@PathVariable Long id) {
        return stakeholderService.findByLocation(id);
    }

        @GetMapping("/names")
    public List<String> getLocationNames() {
        return locationService.findAllNames();
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
