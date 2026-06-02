package be.ucll.unit.service;

import be.ucll.model.Exercise;
import be.ucll.repository.ExerciseRepository;
import be.ucll.service.ExerciseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    private Exercise exercise1;
    private Exercise exercise2;

    @BeforeEach
    void setUp() {
        exercise1 = new Exercise("EX001", "Strength", 3, 10, 5, 45);
        exercise2 = new Exercise("EX002", "Cardio", 2, 5, 3, 30);
    }

    @Test
    void getAllExercises_ReturnsEmptyListWhenNoneAvailable() {
        // Arrange
        when(exerciseRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Exercise> result = exerciseService.getAllExercises();

        // Assert
        assertTrue(result.isEmpty());
        verify(exerciseRepository, times(1)).findAll();
    }

    @Test
    void getAllExercises_ReturnsAllExercisesWhenAvailable() {
        // Arrange
        when(exerciseRepository.findAll()).thenReturn(Arrays.asList(exercise1, exercise2));

        // Act
        List<Exercise> result = exerciseService.getAllExercises();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(exercise1));
        assertTrue(result.contains(exercise2));
        verify(exerciseRepository, times(1)).findAll();
    }

    @Test
    void getAllExercises_RepositoryInteractionVerified() {
        // Act
        exerciseService.getAllExercises();

        // Assert
        verify(exerciseRepository, times(1)).findAll();
        verifyNoMoreInteractions(exerciseRepository);
    }
}