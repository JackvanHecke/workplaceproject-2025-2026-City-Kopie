package be.ucll.unit.service;

import be.ucll.model.CoachLocation;
import be.ucll.repository.CoachLocationRepository;
import be.ucll.service.CoachLocationService;

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
class CoachLocationServiceTest {

    @Mock
    private CoachLocationRepository coachLocationRepository;

    @InjectMocks
    private CoachLocationService coachLocationService;

    @Test
    void getAllCoachLocations_ReturnsEmptyListWhenNoneExist() {
        // Arrange
        when(coachLocationRepository.findAll()).thenReturn(List.of());
        
        // Act
        List<CoachLocation> result = coachLocationService.getAllCoachLocations();
        
        // Assert
        assertTrue(result.isEmpty());
        verify(coachLocationRepository, times(1)).findAll();
    }

    @Test
    void getAllCoachLocations_ReturnsAllExistingLocations() {
        // Arrange
        CoachLocation location1 = new CoachLocation();
        CoachLocation location2 = new CoachLocation();
        when(coachLocationRepository.findAll()).thenReturn(List.of(location1, location2));
        
        // Act
        List<CoachLocation> result = coachLocationService.getAllCoachLocations();
        
        // Assert
        assertEquals(2, result.size());
        verify(coachLocationRepository, times(1)).findAll();
    }

    @Test
    void getCoachLocationById_ExistingId_ReturnsLocation() {
        // Arrange
        Long id = 1L;
        CoachLocation expectedLocation = new CoachLocation();
        when(coachLocationRepository.findById(id)).thenReturn(Optional.of(expectedLocation));
        
        // Act
        CoachLocation result = coachLocationService.getCoachLocationById(id);
        
        // Assert
        assertEquals(expectedLocation, result);
        verify(coachLocationRepository, times(1)).findById(id);
    }

    @Test
    void getCoachLocationById_NonExistingId_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(coachLocationRepository.findById(id)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> coachLocationService.getCoachLocationById(id));
        
        assertEquals("CoachLocation not found with id: 99", exception.getMessage());
        verify(coachLocationRepository, times(1)).findById(id);
    }

    @Test
    void getCoachLocationById_NullId_ThrowsException() {
        // Act & Assert
        assertThrows(RuntimeException.class,
            () -> coachLocationService.getCoachLocationById(null));
    }

    @Test
    void getCoachLocationById_MultipleCalls_VerifiesEachInteraction() {
        // Arrange
        Long id1 = 1L;
        Long id2 = 2L;
        CoachLocation location1 = new CoachLocation();
        CoachLocation location2 = new CoachLocation();
        
        when(coachLocationRepository.findById(id1)).thenReturn(Optional.of(location1));
        when(coachLocationRepository.findById(id2)).thenReturn(Optional.of(location2));
        
        // Act
        CoachLocation result1 = coachLocationService.getCoachLocationById(id1);
        CoachLocation result2 = coachLocationService.getCoachLocationById(id2);
        
        // Assert
        assertEquals(location1, result1);
        assertEquals(location2, result2);
        verify(coachLocationRepository, times(1)).findById(id1);
        verify(coachLocationRepository, times(1)).findById(id2);
    }
}