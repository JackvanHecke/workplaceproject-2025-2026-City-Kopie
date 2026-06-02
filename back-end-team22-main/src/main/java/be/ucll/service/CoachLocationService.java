package be.ucll.service;

import be.ucll.model.CoachLocation;
import be.ucll.repository.CoachLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachLocationService {

    private final CoachLocationRepository coachLocationRepository;

    @Autowired
    public CoachLocationService(CoachLocationRepository coachLocationRepository) {
        this.coachLocationRepository = coachLocationRepository;
    }

    public List<CoachLocation> getAllCoachLocations() {
        return coachLocationRepository.findAll();
    }

    public CoachLocation getCoachLocationById(Long id) {
        return coachLocationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CoachLocation not found with id: " + id));
    }
}
