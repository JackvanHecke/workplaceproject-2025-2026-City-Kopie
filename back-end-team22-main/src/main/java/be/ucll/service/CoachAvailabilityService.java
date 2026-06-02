package be.ucll.service;

import be.ucll.model.CoachAvailability;
import be.ucll.repository.CoachAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachAvailabilityService {

    private final CoachAvailabilityRepository coachAvailabilityRepository;

    @Autowired
    public CoachAvailabilityService(CoachAvailabilityRepository coachAvailabilityRepository) {
        this.coachAvailabilityRepository = coachAvailabilityRepository;
    }

    public List<CoachAvailability> getAllCoachAvailabilities() {
        return coachAvailabilityRepository.findAll();
    }

    public CoachAvailability getCoachAvailabilityById(Long id) {
        return coachAvailabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CoachAvailability not found with id: " + id));
    }
}
