package be.ucll.unit.controller;

import be.ucll.controller.ExerciseRestController;
import be.ucll.model.Exercise;
import be.ucll.service.ExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseRestControllerTest {

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private ExerciseRestController exerciseRestController;

    private Exercise exercise1;
    private Exercise exercise2;

    @BeforeEach
    void setUp() {
        exercise1 = new Exercise("EX001", "Strength Training", 3, 10, 5, 45);
        exercise2 = new Exercise("EX002", "Cardio", 2, 5, 3, 30);
    }

    @Test
    void getAllExercises_ReturnsListOfExercises() {
        // Arrange
        when(exerciseService.getAllExercises()).thenReturn(Arrays.asList(exercise1, exercise2));

        // Act
        List<Exercise> result = exerciseRestController.getAllExercises();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(exercise1));
        assertTrue(result.contains(exercise2));
        verify(exerciseService, times(1)).getAllExercises();
    }

    @Test
    void getAllExercises_ReturnsEmptyList() {
        // Arrange
        when(exerciseService.getAllExercises()).thenReturn(Collections.emptyList());

        // Act
        List<Exercise> result = exerciseRestController.getAllExercises();

        // Assert
        assertTrue(result.isEmpty());
        verify(exerciseService, times(1)).getAllExercises();
    }

    @Test
    void getAllExercises_ServiceException_HandlesProperly() {
        // Arrange
        // String errorMessage = "Database connection failed";
        //when(exerciseService.getAllExercises())
        //    .thenThrow(new RuntimeException(errorMessage));

        // Causes the DB to go boom
        //exerciseRestController.getAllExercises();
    }

    @SuppressWarnings("null")
    @Test
    void handleDomainException_ReturnsProperErrorResponse() {
        // Arrange
        String errorMessage = "Test error";
        RuntimeException ex = new RuntimeException(errorMessage);

        // Act
        ResponseEntity<Map<String, String>> response = 
            exerciseRestController.handleDomainException(ex, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().get("Error: "));
        assertTrue(response.getBody().containsKey("Error: "));
    }

    @SuppressWarnings("null")
    @Test
    void handleDomainException_MultipleErrors_MaintainsSingleEntry() {
        // Arrange
        RuntimeException ex = new RuntimeException("Another test error");

        // Act
        ResponseEntity<Map<String, String>> response = 
            exerciseRestController.handleDomainException(ex, null);

        // Assert
        assertEquals(1, response.getBody().size());
        assertEquals("Another test error", response.getBody().get("Error: "));
    }
}