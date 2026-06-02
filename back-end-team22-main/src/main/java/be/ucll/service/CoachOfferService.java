package be.ucll.service;

import be.ucll.model.CoachOffer;
import be.ucll.repository.CoachOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachOfferService {

    private final CoachOfferRepository coachOfferRepository;

    @Autowired
    public CoachOfferService(CoachOfferRepository coachOfferRepository) {
        this.coachOfferRepository = coachOfferRepository;
    }

    public List<CoachOffer> getAllCoachOffers() {
        return coachOfferRepository.findAll();
    }

    public CoachOffer getCoachOfferById(Long id) {
        return coachOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CoachOffer not found with id: " + id));
    }
}
