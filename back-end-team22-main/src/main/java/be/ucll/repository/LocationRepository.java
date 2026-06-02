package be.ucll.repository;

import be.ucll.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.List;


public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByBenchOwner(String benchOwner);

    @Query("SELECT COUNT(DISTINCT l.benchCountry) FROM Location l")
    long countDistinctCountries();

    default long countAllBenches() {
        return count();
    }

    @Query("select count(l) from Location l where l.createdAt >= :since")
    long countBenchesCreatedSince(@Param("since") Instant since);

        @Query("select l.benchName from Location l order by l.benchName asc")
    List<String> findAllNames();

}

