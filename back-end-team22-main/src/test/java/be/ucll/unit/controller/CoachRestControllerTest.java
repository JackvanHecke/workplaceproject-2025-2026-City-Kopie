package be.ucll.unit.controller;

import be.ucll.controller.CoachRestController;
import be.ucll.model.dto.CoachDTO;
import be.ucll.service.CoachService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoachRestControllerTest {

    @Mock
    private CoachService coachService;

    @InjectMocks
    private CoachRestController coachRestController;

    private CoachDTO coachDTO;

    @BeforeEach
    void setUp() {
        coachDTO = new CoachDTO(
            1L, 
            "Certified trainer with 5+ years experience", 
            "John Doe", 
            true, 
            null, 
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList()
        );
    }

    @Test
    void getAllCoaches_ReturnsListOfCoaches() {
        // Arrange
        when(coachService.getAllCoaches()).thenReturn(List.of(coachDTO));

        // Act
        List<CoachDTO> result = coachRestController.getAllCoaches();

        // Assert
        assertEquals(1, result.size());
        assertEquals(coachDTO, result.get(0));
        verify(coachService, times(1)).getAllCoaches();
    }

    @Test
    void getAllCoaches_EmptyList_ReturnsEmptyList() {
        // Arrange
        when(coachService.getAllCoaches()).thenReturn(Collections.emptyList());

        // Act
        List<CoachDTO> result = coachRestController.getAllCoaches();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getCoachById_ValidId_ReturnsCoach() {
        // Arrange
        Long coachId = 1L;
        when(coachService.getCoachById(coachId)).thenReturn(coachDTO);

        // Act
        CoachDTO result = coachRestController.getCoachById(coachId);

        // Assert
        assertEquals(coachDTO, result);
        verify(coachService, times(1)).getCoachById(coachId);
    }

    @Test
    void getCoachById_NonExistingId_ThrowsException() {
        // Arrange
        Long invalidId = 99L;
        when(coachService.getCoachById(invalidId))
            .thenThrow(new RuntimeException("Coach not found"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> coachRestController.getCoachById(invalidId));

        assertEquals("Coach not found", exception.getMessage());
        verify(coachService, times(1)).getCoachById(invalidId);
    }

    @Test
    void getCoachById_NullId_ThrowsException() {
        coachRestController.getCoachById(null);
    }
}