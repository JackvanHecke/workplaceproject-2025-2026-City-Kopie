package be.ucll.service;

import be.ucll.model.Location;
import be.ucll.model.Stakeholder;
import be.ucll.model.dto.StakeholderDTO;
import be.ucll.repository.LocationRepository;
import be.ucll.repository.StakeholderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StakeholderService {

    private final StakeholderRepository stakeholderRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public StakeholderService(StakeholderRepository stakeholderRepository,
                              LocationRepository locationRepository) {
        this.stakeholderRepository = stakeholderRepository;
        this.locationRepository = locationRepository;
    }

    public Stakeholder createStakeholder(StakeholderDTO dto) {
        Stakeholder stakeholder = new Stakeholder();
        stakeholder.setOrganisation(dto.getOrganisation());
        stakeholder.setContactPerson(dto.getContactPerson());
        stakeholder.setEmail(dto.getEmail());
        stakeholder.setPhoneNumber(dto.getPhoneNumber());
        stakeholder.setRole(dto.getRole());
        stakeholder.setPartnershipDecided(dto.getPartnershipDecided() != null && dto.getPartnershipDecided());
        return stakeholderRepository.save(stakeholder);
    }

    public List<Stakeholder> findAll() {
        return stakeholderRepository.findAll();
    }

    public void deleteStakeholder(Long id) {
        stakeholderRepository.deleteById(id);
    }

    public Stakeholder updateStakeHolder(Long id, StakeholderDTO dto) {
        Stakeholder existing = stakeholderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stakeholder not found: " + id));

        if (dto.getOrganisation() != null) existing.setOrganisation(dto.getOrganisation());
        if (dto.getContactPerson() != null) existing.setContactPerson(dto.getContactPerson());
        if (dto.getEmail() != null) existing.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) existing.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getRole() != null) existing.setRole(dto.getRole());
        if (dto.getPartnershipDecided() != null) existing.setPartnershipDecided(dto.getPartnershipDecided());

        return stakeholderRepository.save(existing);
    }

    // -------------------------
    // NEW: bench-scoped methods
    // -------------------------

    @Transactional(readOnly = true)
    public List<StakeholderDTO> findByLocation(Long benchId) {
        if (!locationRepository.existsById(benchId)) {
            throw new RuntimeException("Location not found with id: " + benchId);
        }

        return stakeholderRepository.findByLocations_BenchId(benchId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void linkStakeholderToLocation(Long benchId, Long stakeholderId) {
        Location location = locationRepository.findById(benchId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + benchId));

        Stakeholder stakeholder = stakeholderRepository.findById(stakeholderId)
                .orElseThrow(() -> new RuntimeException("Stakeholder not found with id: " + stakeholderId));

        boolean alreadyLinked = stakeholder.getLocations().stream()
                .anyMatch(l -> l.getBenchId().equals(location.getBenchId()));

        if (!alreadyLinked) {
            stakeholder.addLocation(location);
            stakeholderRepository.save(stakeholder);
        }
    }

    @Transactional
    public void unlinkStakeholderFromLocation(Long benchId, Long stakeholderId) {
        Location location = locationRepository.findById(benchId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + benchId));

        Stakeholder stakeholder = stakeholderRepository.findById(stakeholderId)
                .orElseThrow(() -> new RuntimeException("Stakeholder not found with id: " + stakeholderId));

        stakeholder.getLocations().removeIf(l -> l.getBenchId().equals(location.getBenchId()));
        location.getStakeholders().removeIf(s -> s.getId().equals(stakeholder.getId()));

        stakeholderRepository.save(stakeholder);
        locationRepository.save(location);
    }

    private StakeholderDTO toDTO(Stakeholder s) {
        return new StakeholderDTO(
                s.getId(),
                s.getOrganisation(),
                s.getContactPerson(),
                s.getEmail(),
                s.getPhoneNumber(),
                s.getRole(),
                s.isPartnershipDecided()
        );
    }
}
