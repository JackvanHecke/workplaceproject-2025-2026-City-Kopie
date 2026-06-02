package be.ucll.unit.model;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.model.*;

public class ExcerciseTest
{
    private Exercise exercise;
    private ProfileExercises profileExercise;

    @BeforeEach
    void setUp() {
        exercise = new Exercise(
            "EX123",
            "Strength Training",
            3,
            10,
            5,
            45
        );

        profileExercise = new ProfileExercises();
        Set<ProfileExercises> profileExercisesSet = new HashSet<>();
        profileExercisesSet.add(profileExercise);
        exercise.setProfileExercises(profileExercisesSet);
    }

    @Test
    void testDefaultConstructor() {
        Exercise defaultExercise = new Exercise();
        assertNull(defaultExercise.getExercise_id());
        assertNull(defaultExercise.getExercise_number());
        assertNull(defaultExercise.getExercise_type());
        assertEquals(0, defaultExercise.getExercise_level());
        assertEquals(0, defaultExercise.getExercise_performed());
        assertEquals(0, defaultExercise.getExercise_favorite());
        assertEquals(0, defaultExercise.getMovement_minutes());
        assertNotNull(defaultExercise.getProfileExercises());
        assertTrue(defaultExercise.getProfileExercises().isEmpty());
    }

    @Test
    void testParameterizedConstructorAndGetters() {
        assertAll(
            () -> assertNull(exercise.getExercise_id()),
            () -> assertEquals("Strength Training", exercise.getExercise_number()),
            () -> assertEquals(null, exercise.getExercise_type()),
            () -> assertEquals(3, exercise.getExercise_level()),
            () -> assertEquals(10, exercise.getExercise_performed()),
            () -> assertEquals(5, exercise.getExercise_favorite()),
            () -> assertEquals(45, exercise.getMovement_minutes()),
            () -> assertNotNull(exercise.getProfileExercises()),
            () -> assertEquals(1, exercise.getProfileExercises().size()),
            () -> assertTrue(exercise.getProfileExercises().contains(profileExercise))
        );
    }

    @Test
    void testSettersAndMutability() {
        exercise.setExercise_id(1L);
        exercise.setExercise_number("EX999");
        exercise.setExercise_type("Cardio");
        exercise.setExercise_level(2);
        exercise.setExercise_performed(20);
        exercise.setExercise_favorite(10);
        exercise.setMovement_minutes(60);
        
        ProfileExercises newProfileExercise = new ProfileExercises();
        Set<ProfileExercises> newProfileExercisesSet = new HashSet<>();
        newProfileExercisesSet.add(newProfileExercise);
        exercise.setProfileExercises(newProfileExercisesSet);

        assertAll(
            () -> assertEquals(1L, exercise.getExercise_id()),
            () -> assertEquals("EX999", exercise.getExercise_number()),
            () -> assertEquals("Cardio", exercise.getExercise_type()),
            () -> assertEquals(2, exercise.getExercise_level()),
            () -> assertEquals(20, exercise.getExercise_performed()),
            () -> assertEquals(10, exercise.getExercise_favorite()),
            () -> assertEquals(60, exercise.getMovement_minutes()),
            () -> assertEquals(1, exercise.getProfileExercises().size()),
            () -> assertTrue(exercise.getProfileExercises().contains(newProfileExercise))
        );
    }

    @Test
    void testMutabilityNegativeAndZeroValues() {
        exercise.setExercise_level(-1);
        exercise.setExercise_performed(0);
        exercise.setExercise_favorite(-5);
        exercise.setMovement_minutes(0);
        
        assertAll(
            () -> assertEquals(-1, exercise.getExercise_level()),
            () -> assertEquals(0, exercise.getExercise_performed()),
            () -> assertEquals(-5, exercise.getExercise_favorite()),
            () -> assertEquals(0, exercise.getMovement_minutes())
        );
    }

    @Test
    void testNullHandling() {
        exercise.setExercise_number(null);
        exercise.setExercise_type(null);
        exercise.setProfileExercises(null);
        
        assertAll(
            () -> assertNull(exercise.getExercise_number()),
            () -> assertNull(exercise.getExercise_type()),
            () -> assertNull(exercise.getProfileExercises())
        );
    }

    @Test
    void testStringEdgeCases() {
        String longString = "A".repeat(255);
        exercise.setExercise_number(longString);
        exercise.setExercise_type(longString);

        assertAll(
            () -> assertEquals(longString, exercise.getExercise_number()),
            () -> assertEquals(longString, exercise.getExercise_type())
        );
    }

    @Test
    void testEmptyStringEdgeCase() {
        exercise.setExercise_number("");
        exercise.setExercise_type("");

        assertAll(
            () -> assertEquals("", exercise.getExercise_number()),
            () -> assertEquals("", exercise.getExercise_type())
        );
    }
    
    @Test
    void testProfileExercisesMutability() {
        ProfileExercises profileExercise2 = new ProfileExercises();
        exercise.getProfileExercises().add(profileExercise2);
        
        assertEquals(2, exercise.getProfileExercises().size());
        assertTrue(exercise.getProfileExercises().contains(profileExercise2));
    }
}