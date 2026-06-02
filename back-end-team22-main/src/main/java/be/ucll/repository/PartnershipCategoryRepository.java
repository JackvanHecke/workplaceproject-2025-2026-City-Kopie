// be/ucll/repository/PartnershipCategoryRepository.java
package be.ucll.repository;

import be.ucll.model.PartnershipCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnershipCategoryRepository extends JpaRepository<PartnershipCategory, Long> {
    Optional<PartnershipCategory> findByCode(String code);
}
