package be.ucll.repository;

import be.ucll.model.CommunicationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommunicationMessageRepository extends JpaRepository<CommunicationMessage, Long> {

    // Active in time window + marked active
    List<CommunicationMessage> findByActiveIsTrueAndStartsAtBeforeAndEndsAtAfter(
            LocalDateTime now1, LocalDateTime now2
    );

    // Active with open-ended end date
    List<CommunicationMessage> findByActiveIsTrueAndStartsAtBeforeAndEndsAtIsNull(LocalDateTime now);
    List<CommunicationMessage> findByActiveIsTrue();

}
