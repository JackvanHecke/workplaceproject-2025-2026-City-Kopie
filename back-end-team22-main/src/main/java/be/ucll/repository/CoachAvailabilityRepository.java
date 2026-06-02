package be.ucll.repository;

import be.ucll.model.CoachAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface CoachAvailabilityRepository extends JpaRepository<CoachAvailability, Long> {
    List<CoachAvailability> findByCoachId(Long coachId);
    // Example useful query: find availabilities that cover a given datetime
    List<CoachAvailability> findByBench_BenchIdAndAvailableFromBeforeAndAvailableToAfter(
            Long benchId, LocalDateTime from, LocalDateTime to
    );
}
