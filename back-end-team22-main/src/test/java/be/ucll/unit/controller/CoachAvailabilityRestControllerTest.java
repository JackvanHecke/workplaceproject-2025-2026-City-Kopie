package be.ucll.unit.controller;

import be.ucll.controller.CoachAvailabilityRestController;
import be.ucll.model.CoachAvailability;
import be.ucll.service.CoachAvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoachAvailabilityRestControllerTest {

    @Mock
    private CoachAvailabilityService coachAvailabilityService;

    @InjectMocks
    private CoachAvailabilityRestController controller;

    private CoachAvailability availability;

    @BeforeEach
    void setUp() {
        availability = new CoachAvailability();
    }

    @Test
    void getAllCoachAvailabilities_ReturnsAllAvailabilities() {
        // Arrange
        when(coachAvailabilityService.getAllCoachAvailabilities()).thenReturn(List.of(availability));
        
        // Act
        List<CoachAvailability> response = controller.getAllCoachAvailabilities();
        
        // Assert
        assertEquals(1, response.size());
        assertEquals(availability, response.get(0));
        verify(coachAvailabilityService, times(1)).getAllCoachAvailabilities();
    }

    @Test
    void getCoachAvailabilityById_ExistingId_ReturnsAvailability() {
        // Arrange
        when(coachAvailabilityService.getCoachAvailabilityById(1L)).thenReturn(availability);
        
        // Act
        CoachAvailability response = controller.getCoachAvailabilityById(1L);
        
        // Assert
        assertEquals(availability, response);
        verify(coachAvailabilityService, times(1)).getCoachAvailabilityById(1L);
    }

    @Test
    void getCoachAvailabilityById_NonExistingId_ThrowsException() {
        // Arrange
        String errorMessage = "CoachAvailability not found with id: 99";
        when(coachAvailabilityService.getCoachAvailabilityById(99L))
            .thenThrow(new RuntimeException(errorMessage));
        assertThrows(RuntimeException.class, () -> controller.getCoachAvailabilityById(99L));
    }

    @Test
    void handleDomainException_RuntimeException_ReturnsErrorResponse() {
        // Arrange
        String errorMessage = "Test error";
        RuntimeException ex = new RuntimeException(errorMessage);
        
        // Act
        ResponseEntity<Map<String, String>> response = 
            controller.handleDomainException(ex, null);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAllCoachAvailabilities_EmptyResult_ReturnsEmptyList() {
        // Arrange
        when(coachAvailabilityService.getAllCoachAvailabilities()).thenReturn(List.of());
        
        // Act
        List<CoachAvailability> response = controller.getAllCoachAvailabilities();
        
        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void getCoachAvailabilityById_NullId_ThrowsException() {
        assertNull(controller.getCoachAvailabilityById(null));
    }
}