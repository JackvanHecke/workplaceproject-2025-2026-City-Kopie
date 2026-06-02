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

import be.ucll.model.CoachOffer;
import be.ucll.service.CoachOfferService;

@RestController
@RequestMapping("/coach-offers")
@CrossOrigin(origins = "http://localhost:8000")
public class CoachOfferRestController {

    private final CoachOfferService coachOfferService;

    public CoachOfferRestController(CoachOfferService coachOfferService) {
        this.coachOfferService = coachOfferService;
    }

    @GetMapping
    public List<CoachOffer> getAllCoachOffers() {
        return coachOfferService.getAllCoachOffers();
    }

    @GetMapping("/{id}")
    public CoachOffer getCoachOfferById(@PathVariable Long id) {
        return coachOfferService.getCoachOfferById(id);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(RuntimeException ex, WebRequest request) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("Error: ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
