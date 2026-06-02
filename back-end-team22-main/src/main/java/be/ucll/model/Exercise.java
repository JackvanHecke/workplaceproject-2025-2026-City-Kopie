package be.ucll.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "EXERCISES")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exercise_id;

    private String exercise_number;
    private String exercise_type;
    private int exercise_level;
    private int exercise_performed;
    private int exercise_favorite;
    private int movement_minutes;

    @OneToMany(mappedBy = "exercise")
    private Set<ProfileExercises> profileExercises = new HashSet<>();

    public Exercise() {}

    public Exercise(String exerciseNumber, String exerciseType, int exerciseLevel, int exercisePerformed, int exerciseFavorite, int movementMinutes) {
        this.exercise_number = exerciseNumber;
        this.exercise_number = exerciseType;
        this.exercise_level = exerciseLevel;
        this.exercise_performed = exercisePerformed;
        this.exercise_favorite = exerciseFavorite;
        this.movement_minutes = movementMinutes;
    }

    public Long getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(Long exercise_id) {
        this.exercise_id = exercise_id;
    }

    public String getExercise_number() {
        return exercise_number;
    }

    public void setExercise_number(String exercise_number) {
        this.exercise_number = exercise_number;
    }

    public String getExercise_type() {
        return exercise_type;
    }

    public void setExercise_type(String exercise_type) {
        this.exercise_type = exercise_type;
    }

    public int getExercise_level() {
        return exercise_level;
    }

    public void setExercise_level(int exercise_level) {
        this.exercise_level = exercise_level;
    }

    public int getExercise_performed() {
        return exercise_performed;
    }

    public void setExercise_performed(int exercise_performed) {
        this.exercise_performed = exercise_performed;
    }

    public int getExercise_favorite() {
        return exercise_favorite;
    }

    public void setExercise_favorite(int exercise_favorite) {
        this.exercise_favorite = exercise_favorite;
    }

    public int getMovement_minutes() {
        return movement_minutes;
    }

    public void setMovement_minutes(int movement_minutes) {
        this.movement_minutes = movement_minutes;
    }

    public Set<ProfileExercises> getProfileExercises() {
        return profileExercises;
    }

    public void setProfileExercises(Set<ProfileExercises> profileExercises) {
        this.profileExercises = profileExercises;
    }

 }
