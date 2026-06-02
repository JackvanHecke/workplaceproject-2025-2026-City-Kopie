package be.ucll.unit.service;

import be.ucll.model.*;
import be.ucll.model.dto.*;
import be.ucll.repository.CoachRepository;
import be.ucll.service.CoachService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoachServiceTest {

    @Mock
    private CoachRepository coachRepository;

    @InjectMocks
    private CoachService coachService;

    private Coach coach;
    private Profile profile;
    private Location location;
    private CoachOffer offer;
    private CoachAvailability availability;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profile.setName("John Doe");

        coach = new Coach();
        coach.setProfile(profile);
        coach.setBio("Certified trainer");
        coach.setIsAvailable(true);
        coach.setCreatedAt(LocalDateTime.now());

        location = new Location();
        location.setBenchId(10L);
        location.setBenchName("Central Park");
        location.setBenchCity("New York");
        coach.addLocation(location);

        offer = new CoachOffer();
        offer.setOfferType("Workshop");
        offer.setFreeOrPaid(CoachOffer.FeeType.paid);
        coach.addOffer(offer);

        availability = new CoachAvailability();
        availability.setBench(location);
        coach.addAvailability(availability);
    }

    @Test
    void getAllCoaches_ReturnsAllMappedCoaches() {
        // Arrange
        when(coachRepository.findAll()).thenReturn(List.of(coach));
        
        // Act
        List<CoachDTO> result = coachService.getAllCoaches();
        
        // Assert
        assertEquals(1, result.size());
        CoachDTO dto = result.get(0);
        assertFullMappingCorrectness(dto);
        verify(coachRepository, times(1)).findAll();
    }

    @Test
    void getCoachById_ExistingId_ReturnsMappedCoach() {
        // Arrange
        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        
        // Act
        CoachDTO result = coachService.getCoachById(1L);
        
        // Assert
        assertFullMappingCorrectness(result);
        verify(coachRepository, times(1)).findById(1L);
    }

    @Test
    void getCoachById_NonExistingId_ThrowsException() {
        // Arrange
        when(coachRepository.findById(99L)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> coachService.getCoachById(99L));
        
        assertEquals("Coach not found with id: 99", exception.getMessage());
    }

    @Test
    void toDTO_NullCollections_UsesEmptyCollections() {
        // Arrange
        coach.setLocations(null);
        coach.setOffers(null);
        coach.setAvailability(null);
        
        // Act
        CoachDTO dto = coachService.toDTO(coach);
        
        // Assert
        assertTrue(dto.getLocations().isEmpty());
        assertTrue(dto.getOffers().isEmpty());
        assertTrue(dto.getAvailability().isEmpty());
    }

    @Test
    void toDTO_NullProfile_HandlesGracefully() {
        // Arrange
        coach.setProfile(null);
        
        // Act
        CoachDTO dto = coachService.toDTO(coach);
        
        // Assert
        assertNull(dto.getName());
        assertNull(dto.getId());
    }

    @Test
    void toDTO_LocationMapping_CorrectFieldsMapped() {
        // Act
        CoachDTO dto = coachService.toDTO(coach);
        
        // Assert - Check first location in DTO
        LocationDTO locDTO = dto.getLocations().get(0);
        assertEquals(location.getBenchId(), locDTO.getId());
        assertEquals(location.getBenchName(), locDTO.getName());
        assertEquals(location.getBenchCity(), locDTO.getCity());
        assertNull(locDTO.getPhotoUrl()); // Verify null fields
    }

    @Test
    void toDTO_OfferMapping_ConvertsCorrectly() {
        // Act
        CoachDTO dto = coachService.toDTO(coach);
        
        // Assert - Check first offer in DTO
        CoachOfferDTO offerDTO = dto.getOffers().get(0);
        assertEquals(offer.getId(), offerDTO.getId());
        assertEquals(offer.getOfferType(), offerDTO.getOfferType());
        assertEquals("paid", offerDTO.getFreeOrPaid());
    }

    @Test
    void toDTO_AvailabilityWithBenchMapping_CorrectNestedDTO() {
        // Act
        CoachDTO dto = coachService.toDTO(coach);
        
        // Assert - Check availability bench mapping
        CoachAvailabilityDTO availDTO = dto.getAvailability().get(0);
        LocationDTO benchDTO = availDTO.getBench();
        
        assertEquals(location.getBenchId(), benchDTO.getId());
        assertEquals(location.getBenchName(), benchDTO.getName());
        assertEquals(location.getBenchCity(), benchDTO.getCity());
    }

    @Test
    void toDTO_AvailabilityWithoutBench_NullBenchInDTO() {
        // Arrange
        availability.setBench(null);
        
        // Act
        CoachDTO dto = coachService.toDTO(coach);
        
        // Assert
        assertNull(dto.getAvailability().get(0).getBench());
    }

    @Test
    void toDTO_EmptyOffersButNonNull_ReturnsEmptyList() {
        // Arrange
        coach.setOffers(new HashSet<>());
        
        // Act
        CoachDTO dto = coachService.toDTO(coach);
        
        // Assert
        assertTrue(dto.getOffers().isEmpty());
    }

    @Test
    void toDTO_MinimalCoach_AllFieldsPresent() {
        // Arrange
        Coach minimal = new Coach();
        minimal.setIsAvailable(false);
        
        // Act
        CoachDTO dto = coachService.toDTO(minimal);
        
        // Assert
        assertEquals(null, dto.getId());
        assertFalse(dto.getIsAvailable());
        assertNull(dto.getName());
        assertTrue(dto.getLocations().isEmpty());
    }

    private void assertFullMappingCorrectness(CoachDTO dto) {
        assertEquals(coach.getId(), dto.getId());
        assertEquals(profile.getName(), dto.getName());
        assertEquals(coach.getBio(), dto.getBio());
        assertEquals(coach.getIsAvailable(), dto.getIsAvailable());
        assertEquals(coach.getCreatedAt(), dto.getCreatedAt());
        
        // Locations
        assertEquals(1, dto.getLocations().size());
        LocationDTO locDTO = dto.getLocations().get(0);
        assertEquals(location.getBenchId(), locDTO.getId());
        
        // Offers
        assertEquals(1, dto.getOffers().size());
        CoachOfferDTO offerDTO = dto.getOffers().get(0);
        assertEquals(offer.getId(), offerDTO.getId());
        
        // Availability
        assertEquals(1, dto.getAvailability().size());
        CoachAvailabilityDTO availDTO = dto.getAvailability().get(0);
        assertEquals(availability.getId(), availDTO.getId());
    }

    @Test
    void toDTO_OfferMapping_WithNullFreeOrPaid_ConvertsToNull() {
        // Arrange
        offer.setFreeOrPaid(null); // Explicitly set to null

        // Act
        CoachDTO dto = coachService.toDTO(coach);
        
        // Assert
        CoachOfferDTO offerDTO = dto.getOffers().get(0);
        assertNull(offerDTO.getFreeOrPaid());  // Should be null

        // Also verify non-null mapper as a safety check
        offer.setFreeOrPaid(CoachOffer.FeeType.free);
        CoachDTO dto2 = coachService.toDTO(coach);
        assertEquals("free", dto2.getOffers().get(0).getFreeOrPaid());
    }

    @Test
    void toDTO_OfferMapping_NonNullFreeOrPaid_ConvertsToEnumName() {
        // Arrange
        offer.setFreeOrPaid(CoachOffer.FeeType.paid);

        // Act
        CoachDTO dto = coachService.toDTO(coach);
        
        // Assert
        CoachOfferDTO offerDTO = dto.getOffers().get(0);
        assertEquals("paid", offerDTO.getFreeOrPaid());
        
        // Change enum and reprocess
        offer.setFreeOrPaid(CoachOffer.FeeType.free);
        CoachDTO dtoWhenFree = coachService.toDTO(coach);
        assertEquals("free", dtoWhenFree.getOffers().get(0).getFreeOrPaid());
    }
}