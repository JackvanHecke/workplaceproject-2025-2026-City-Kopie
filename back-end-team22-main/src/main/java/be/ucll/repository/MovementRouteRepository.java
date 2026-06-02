// be.ucll.repository.MovementRouteRepository
package be.ucll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.model.MovementRoute;

public interface MovementRouteRepository extends JpaRepository<MovementRoute, Long> {
}
