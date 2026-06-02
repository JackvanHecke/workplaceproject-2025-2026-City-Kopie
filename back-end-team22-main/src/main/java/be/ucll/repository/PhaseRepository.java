package be.ucll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.model.Phase;

public interface PhaseRepository extends JpaRepository<Phase, Long> {

    public Phase findByName(String name);
    
}