package be.ucll.unit.service;

import be.ucll.model.CoachOffer;
import be.ucll.repository.CoachOfferRepository;
import be.ucll.service.CoachOfferService;

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
class CoachOfferServiceTest {

    @Mock
    private CoachOfferRepository coachOfferRepository;

    @InjectMocks
    private CoachOfferService coachOfferService;

    @Test
    void getAllCoachOffers_ReturnsEmptyListWhenNoneExist() {
        // Arrange
        when(coachOfferRepository.findAll()).thenReturn(List.of());
        
        // Act
        List<CoachOffer> result = coachOfferService.getAllCoachOffers();
        
        // Assert
        assertTrue(result.isEmpty());
        verify(coachOfferRepository, times(1)).findAll();
    }

    @Test
    void getAllCoachOffers_ReturnsAllExistingOffers() {
        // Arrange
        CoachOffer offer1 = new CoachOffer();
        CoachOffer offer2 = new CoachOffer();
        when(coachOfferRepository.findAll()).thenReturn(List.of(offer1, offer2));
        
        // Act
        List<CoachOffer> result = coachOfferService.getAllCoachOffers();
        
        // Assert
        assertEquals(2, result.size());
        verify(coachOfferRepository, times(1)).findAll();
    }

    @Test
    void getCoachOfferById_ExistingId_ReturnsOffer() {
        // Arrange
        Long id = 1L;
        CoachOffer expectedOffer = new CoachOffer();
        when(coachOfferRepository.findById(id)).thenReturn(Optional.of(expectedOffer));
        
        // Act
        CoachOffer result = coachOfferService.getCoachOfferById(id);
        
        // Assert
        assertEquals(expectedOffer, result);
        verify(coachOfferRepository, times(1)).findById(id);
    }

    @Test
    void getCoachOfferById_NonExistingId_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(coachOfferRepository.findById(id)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> coachOfferService.getCoachOfferById(id));
        
        assertEquals("CoachOffer not found with id: 99", exception.getMessage());
        verify(coachOfferRepository, times(1)).findById(id);
    }

    @Test
    void getCoachOfferById_NullId_ThrowsException() {
        // Act & Assert
        assertThrows(RuntimeException.class,
            () -> coachOfferService.getCoachOfferById(null));
    }

    @Test
    void getCoachOfferById_MultipleCalls_VerifiesRepositoryInteractions() {
        // Arrange
        Long id1 = 1L;
        Long id2 = 2L;
        CoachOffer offer1 = new CoachOffer();
        CoachOffer offer2 = new CoachOffer();
        
        when(coachOfferRepository.findById(id1)).thenReturn(Optional.of(offer1));
        when(coachOfferRepository.findById(id2)).thenReturn(Optional.of(offer2));
        
        // Act
        CoachOffer result1 = coachOfferService.getCoachOfferById(id1);
        CoachOffer result2 = coachOfferService.getCoachOfferById(id2);
        
        // Assert
        assertEquals(offer1, result1);
        assertEquals(offer2, result2);
        verify(coachOfferRepository, times(1)).findById(id1);
        verify(coachOfferRepository, times(1)).findById(id2);
    }
}