// be/ucll/service/LocationPartnershipService.java
package be.ucll.service;

import be.ucll.model.Location;
import be.ucll.model.LocationPartnership;
import be.ucll.model.LocationPartnershipId;
import be.ucll.model.PartnershipCategory;
import be.ucll.model.dto.LocationPartnershipDTO;
import be.ucll.repository.LocationPartnershipRepository;
import be.ucll.repository.LocationRepository;
import be.ucll.repository.PartnershipCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationPartnershipService {

    private final LocationPartnershipRepository lpRepo;
    private final LocationRepository locationRepo;
    private final PartnershipCategoryRepository categoryRepo;

    public LocationPartnershipService(
            LocationPartnershipRepository lpRepo,
            LocationRepository locationRepo,
            PartnershipCategoryRepository categoryRepo
    ) {
        this.lpRepo = lpRepo;
        this.locationRepo = locationRepo;
        this.categoryRepo = categoryRepo;
    }

    @Transactional(readOnly = true)
    public List<LocationPartnershipDTO> getForLocation(Long benchId) {
        if (!locationRepo.existsById(benchId)) {
            throw new RuntimeException("Location not found with id: " + benchId);
        }
        return lpRepo.findByLocation_BenchId(benchId).stream()
                .map(lp -> new LocationPartnershipDTO(lp.getCategory().getId(), lp.isDecided()))
                .toList();
    }

    @Transactional
    public LocationPartnershipDTO setDecided(Long benchId, Long categoryId, boolean decided) {
        Location location = locationRepo.findById(benchId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + benchId));

        PartnershipCategory category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        LocationPartnershipId id = new LocationPartnershipId(benchId, categoryId);

        LocationPartnership lp = lpRepo.findById(id).orElseGet(() -> {
            LocationPartnership fresh = new LocationPartnership();
            fresh.setId(id);
            fresh.setLocation(location);
            fresh.setCategory(category);
            fresh.setDecided(false);
            return fresh;
        });

        lp.setDecided(decided);
        lpRepo.save(lp);

        return new LocationPartnershipDTO(categoryId, decided);
    }

    @Transactional
    public LocationPartnershipDTO toggle(Long benchId, Long categoryId) {
        // toggle based on current value
        LocationPartnershipId id = new LocationPartnershipId(benchId, categoryId);
        boolean newValue = lpRepo.findById(id).map(lp -> !lp.isDecided()).orElse(true);
        return setDecided(benchId, categoryId, newValue);
    }
}
