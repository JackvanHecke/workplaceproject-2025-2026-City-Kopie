package be.ucll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.model.ProfileExercises;


public interface ProfileExerciseRepository  extends JpaRepository<ProfileExercises, Long> {
    
}
