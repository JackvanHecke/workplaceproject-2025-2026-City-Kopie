package be.ucll.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.model.Phase;
import be.ucll.repository.PhaseRepository;

@Service
public class PhaseService {
    private PhaseRepository phaseRepository;

    @Autowired
    public PhaseService(PhaseRepository phaseRepository) {
        this.phaseRepository = phaseRepository;
    }

    public List<Phase> getAllPhases() {
        return phaseRepository.findAll();
    }
}
