package be.ucll.controller;

import be.ucll.model.dto.CoachDTO;
import be.ucll.service.CoachService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coaches")
@CrossOrigin(origins = "http://localhost:8000")
public class CoachRestController {

    private final CoachService coachService;

    public CoachRestController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping
    public List<CoachDTO> getAllCoaches() {
        return coachService.getAllCoaches();
    }

    @GetMapping("/{id}")
    public CoachDTO getCoachById(@PathVariable Long id) {
        return coachService.getCoachById(id);
    }
}
