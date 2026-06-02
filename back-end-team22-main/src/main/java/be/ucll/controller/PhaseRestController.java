package be.ucll.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.model.Phase;
import be.ucll.service.PhaseService;

@RestController
@RequestMapping("/phases")
@CrossOrigin(origins = "http://localhost:8000")
public class PhaseRestController {
    private final PhaseService phaseService;

    public PhaseRestController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    @GetMapping
    public List<Phase> getAllPhases() {
        return phaseService.getAllPhases();
    }
}
