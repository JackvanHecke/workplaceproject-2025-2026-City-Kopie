package be.ucll.repository;

import be.ucll.model.CoachOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CoachOfferRepository extends JpaRepository<CoachOffer, Long> {
    List<CoachOffer> findByCoachId(Long coachId);
}
