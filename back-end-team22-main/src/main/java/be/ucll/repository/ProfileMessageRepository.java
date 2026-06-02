package be.ucll.repository;

import be.ucll.model.ProfileMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileMessageRepository extends JpaRepository<ProfileMessage, Long> {

    // last N messages for a profile
    List<ProfileMessage> findTop4ByProfileIdOrderByDeliveredAtDesc(Long profileId);

    List<ProfileMessage> findByProfileIdOrderByDeliveredAtDesc(Long profileId);

    boolean existsByProfileIdAndMessageId(Long profileId, Long messageId);

    void deleteByMessageId(Long messageId);
}
