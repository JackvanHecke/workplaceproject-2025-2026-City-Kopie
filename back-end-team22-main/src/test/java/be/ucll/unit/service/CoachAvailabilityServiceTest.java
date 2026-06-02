package be.ucll.unit.service;

import be.ucll.model.CoachAvailability;
import be.ucll.repository.CoachAvailabilityRepository;
import be.ucll.service.CoachAvailabilityService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoachAvailabilityServiceTest {

    @Mock
    private CoachAvailabilityRepository coachAvailabilityRepository;

    @InjectMocks
    private CoachAvailabilityService coachAvailabilityService;

    @Test
    void getAllCoachAvailabilities_ReturnsEmptyListWhenNoneExist() {
        // Arrange
        when(coachAvailabilityRepository.findAll()).thenReturn(List.of());
        
        // Act
        List<CoachAvailability> result = coachAvailabilityService.getAllCoachAvailabilities();
        
        // Assert
        assertTrue(result.isEmpty());
        verify(coachAvailabilityRepository, times(1)).findAll();
    }

    @Test
    void getAllCoachAvailabilities_ReturnsAllWhenAvailable() {
        // Arrange
        CoachAvailability availability1 = new CoachAvailability();
        CoachAvailability availability2 = new CoachAvailability();
        when(coachAvailabilityRepository.findAll()).thenReturn(List.of(availability1, availability2));
        
        // Act
        List<CoachAvailability> result = coachAvailabilityService.getAllCoachAvailabilities();
        
        // Assert
        assertEquals(2, result.size());
        verify(coachAvailabilityRepository, times(1)).findAll();
    }

    @Test
    void getCoachAvailabilityById_ExistingId_ReturnsAvailability() {
        // Arrange
        Long id = 1L;
        CoachAvailability availability = new CoachAvailability();
        when(coachAvailabilityRepository.findById(id)).thenReturn(Optional.of(availability));
        
        // Act
        CoachAvailability result = coachAvailabilityService.getCoachAvailabilityById(id);
        
        // Assert
        assertEquals(availability, result);
        verify(coachAvailabilityRepository, times(1)).findById(id);
    }

    @Test
    void getCoachAvailabilityById_NonExistingId_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(coachAvailabilityRepository.findById(id)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> coachAvailabilityService.getCoachAvailabilityById(id));
        
        assertEquals("CoachAvailability not found with id: 99", exception.getMessage());
        verify(coachAvailabilityRepository, times(1)).findById(id);
    }

    @Test
    void getCoachAvailabilityById_NullId_ThrowsException() {
        // Act & Assert
        assertThrows(RuntimeException.class,
            () -> coachAvailabilityService.getCoachAvailabilityById(null));
    }
}