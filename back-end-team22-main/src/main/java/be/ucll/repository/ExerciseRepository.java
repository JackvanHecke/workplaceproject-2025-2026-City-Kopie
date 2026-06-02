package be.ucll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.model.Exercise;


public interface ExerciseRepository  extends JpaRepository<Exercise, Long> {
    
}
