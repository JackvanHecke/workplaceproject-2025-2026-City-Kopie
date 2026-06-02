// be/ucll/repository/LocationPartnershipRepository.java
package be.ucll.repository;

import be.ucll.model.LocationPartnership;
import be.ucll.model.LocationPartnershipId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationPartnershipRepository extends JpaRepository<LocationPartnership, LocationPartnershipId> {
    List<LocationPartnership> findByLocation_BenchId(Long benchId);
}
