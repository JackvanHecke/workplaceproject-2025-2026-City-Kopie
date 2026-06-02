package be.ucll.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import be.ucll.model.CoachAvailability;
import be.ucll.service.CoachAvailabilityService;

@RestController
@RequestMapping("/coach-availabilities")
@CrossOrigin(origins = "http://localhost:8000")
public class CoachAvailabilityRestController {

    private final CoachAvailabilityService coachAvailabilityService;

    public CoachAvailabilityRestController(CoachAvailabilityService coachAvailabilityService) {
        this.coachAvailabilityService = coachAvailabilityService;
    }

    @GetMapping
    public List<CoachAvailability> getAllCoachAvailabilities() {
        return coachAvailabilityService.getAllCoachAvailabilities();
    }

    @GetMapping("/{id}")
    public CoachAvailability getCoachAvailabilityById(@PathVariable Long id) {
        return coachAvailabilityService.getCoachAvailabilityById(id);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(RuntimeException ex, WebRequest request) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("Error: ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
