package be.ucll.repository;

import be.ucll.model.Stakeholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StakeholderRepository extends JpaRepository<Stakeholder, Long> {

        // JpaRepository already provides findById(..), so this is unnecessary,
        // but leaving it doesn't break anything:
        Stakeholder findStakeholderById(Long id);

        // NEW: fetch stakeholders linked to a specific bench/location
        List<Stakeholder> findByLocations_BenchId(Long benchId);
}
