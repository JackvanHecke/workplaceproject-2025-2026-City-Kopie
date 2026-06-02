package be.ucll.unit.controller;

import be.ucll.controller.CoachLocationRestController;
import be.ucll.model.CoachLocation;
import be.ucll.service.CoachLocationService;
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
class CoachLocationRestControllerTest {

    @Mock
    private CoachLocationService coachLocationService;

    @InjectMocks
    private CoachLocationRestController controller;

    private CoachLocation location1;
    private CoachLocation location2;

    @BeforeEach
    void setUp() {
        location1 = new CoachLocation();

        location2 = new CoachLocation();
    }

    @Test
    void getAllCoachLocations_ReturnsAllLocations() {
        // Arrange
        when(coachLocationService.getAllCoachLocations()).thenReturn(List.of(location1, location2));

        // Act
        List<CoachLocation> result = controller.getAllCoachLocations();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(location1) && result.contains(location2));
        verify(coachLocationService, times(1)).getAllCoachLocations();
    }

    @Test
    void getAllCoachLocations_EmptyList_ReturnsEmptyList() {
        // Arrange
        when(coachLocationService.getAllCoachLocations()).thenReturn(List.of());

        // Act
        List<CoachLocation> result = controller.getAllCoachLocations();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getCoachLocationById_ValidId_ReturnsLocation() {
        // Arrange
        when(coachLocationService.getCoachLocationById(1L)).thenReturn(location1);

        // Act
        CoachLocation result = controller.getCoachLocationById(1L);

        // Assert
        assertEquals(location1, result);
        verify(coachLocationService, times(1)).getCoachLocationById(1L);
    }

    @Test
    void getCoachLocationById_InvalidId_ThrowsException() {
        // Arrange
        String errorMessage = "CoachLocation not found with id: 99";
        when(coachLocationService.getCoachLocationById(99L))
            .thenThrow(new RuntimeException(errorMessage));

        assertThrows(RuntimeException.class, () -> controller.getCoachLocationById(99L));
    }

    @Test
    void getCoachLocationById_ServiceException_HandlesProperly() {
        // Arrange
        String errorMessage = "Database error";
        when(coachLocationService.getCoachLocationById(3L))
            .thenThrow(new RuntimeException(errorMessage));

        assertThrows(RuntimeException.class, () -> controller.getCoachLocationById(3L));
    }

    @SuppressWarnings("null")
    @Test
    void handleDomainException_ReturnsProperErrorResponse() {
        // Arrange
        String errorMessage = "Test error message";
        RuntimeException ex = new RuntimeException(errorMessage);

        // Act
        ResponseEntity<Map<String, String>> response = 
            controller.handleDomainException(ex, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().get("Error: "));
        assertEquals(1, response.getBody().size());
    }

    @Test
    void multipleGetRequests_VerifyServiceInteraction() {
        // Arrange
        when(coachLocationService.getCoachLocationById(1L)).thenReturn(location1);
        when(coachLocationService.getCoachLocationById(2L)).thenReturn(location2);

        // Act
        CoachLocation result1 = controller.getCoachLocationById(1L);
        CoachLocation result2 = controller.getCoachLocationById(2L);

        // Assert
        assertEquals(location1, result1);
        assertEquals(location2, result2);
        verify(coachLocationService, times(1)).getCoachLocationById(1L);
        verify(coachLocationService, times(1)).getCoachLocationById(2L);
    }
}