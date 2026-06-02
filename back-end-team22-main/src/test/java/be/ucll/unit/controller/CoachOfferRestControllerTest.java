package be.ucll.unit.controller;

import be.ucll.controller.CoachOfferRestController;
import be.ucll.model.CoachOffer;
import be.ucll.service.CoachOfferService;
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
class CoachOfferRestControllerTest {

    @Mock
    private CoachOfferService coachOfferService;

    @InjectMocks
    private CoachOfferRestController controller;

    private CoachOffer offer1;
    private CoachOffer offer2;

    @BeforeEach
    void setUp() {
        offer1 = new CoachOffer();
        offer2 = new CoachOffer();
    }

    @Test
    void getAllCoachOffers_ReturnsAllOffers() {
        // Arrange
        when(coachOfferService.getAllCoachOffers()).thenReturn(List.of(offer1, offer2));

        // Act
        List<CoachOffer> response = controller.getAllCoachOffers();

        // Assert
        assertEquals(2, response.size());
        assertTrue(response.contains(offer1));
        verify(coachOfferService, times(1)).getAllCoachOffers();
    }

    @Test
    void getAllCoachOffers_EmptyList_ReturnsEmptyList() {
        // Arrange
        when(coachOfferService.getAllCoachOffers()).thenReturn(List.of());

        // Act
        List<CoachOffer> response = controller.getAllCoachOffers();

        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void getAllCoachOffers_ServiceThrowsException_HandlesError() {
        // Arrange
        String error = "Database error";
        when(coachOfferService.getAllCoachOffers()).thenThrow(new RuntimeException(error));

        // Act
        assertThrows(RuntimeException.class,() -> controller.getAllCoachOffers());
    }

    @Test
    void getCoachOfferById_ExistingId_ReturnsOffer() {
        // Arrange
        Long id = 1L;
        when(coachOfferService.getCoachOfferById(id)).thenReturn(offer1);

        // Act
        CoachOffer response = controller.getCoachOfferById(id);

        // Assert
        assertEquals(offer1, response);
        verify(coachOfferService, times(1)).getCoachOfferById(id);
    }

    @Test
    void getCoachOfferById_NonExistingId_ThrowsException() {
        // Arrange
        Long id = 99L;
        String error = "CoachOffer not found with id: 99";
        when(coachOfferService.getCoachOfferById(id)).thenThrow(new RuntimeException(error));

        assertThrows(RuntimeException.class, () -> controller.getCoachOfferById(id));
    }

    @Test
    void getCoachOfferById_NullId_ThrowsException() {
        // Act & Assert
        controller.getCoachOfferById(null);
    }

    @SuppressWarnings("null")
    @Test
    void handleDomainException_ReturnsConsistentErrorFormat() {
        // Arrange
        String error = "Test error";
        RuntimeException ex = new RuntimeException(error);

        // Act
        ResponseEntity<Map<String, String>> response = 
            controller.handleDomainException(ex, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(error, response.getBody().get("Error: "));
    }
}