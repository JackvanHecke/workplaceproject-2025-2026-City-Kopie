package be.ucll.repository;

import be.ucll.model.CoachLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachLocationRepository extends JpaRepository<CoachLocation, Long> {
}
