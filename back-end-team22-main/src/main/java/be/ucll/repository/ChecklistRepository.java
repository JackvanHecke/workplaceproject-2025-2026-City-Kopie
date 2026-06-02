package be.ucll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.model.Checklist;


public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    Checklist findByName(String name);
    
}
