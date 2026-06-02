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

import be.ucll.model.CoachLocation;
import be.ucll.service.CoachLocationService;

@RestController
@RequestMapping("/coach-locations")
@CrossOrigin(origins = "http://localhost:8000")
public class CoachLocationRestController {

    private final CoachLocationService coachLocationService;

    public CoachLocationRestController(CoachLocationService coachLocationService) {
        this.coachLocationService = coachLocationService;
    }

    @GetMapping
    public List<CoachLocation> getAllCoachLocations() {
        return coachLocationService.getAllCoachLocations();
    }

    @GetMapping("/{id}")
    public CoachLocation getCoachLocationById(@PathVariable Long id) {
        return coachLocationService.getCoachLocationById(id);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(RuntimeException ex, WebRequest request) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("Error: ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
