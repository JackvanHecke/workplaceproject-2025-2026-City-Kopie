// package be.ucll.unit.service;

// import be.ucll.model.Location;
// import be.ucll.model.dto.LocationDTO;
// import be.ucll.repository.LocationRepository;
// import be.ucll.service.LocationService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class LocationServiceTest {

//     @Mock
//     private LocationRepository locationRepository;

//     @InjectMocks
//     private LocationService locationService;

//     private Location location;
//     private LocationDTO locationDTO;

//     @BeforeEach
//     void setUp() {
//         location = new Location(
//         );

//         location.setBenchName(
//             "Park Bench"
//         );
//         location.setBenchOwner(
//             "City Council"
//         );
//         location.setBenchCity(
//             "Park Street"
//         );
//         location.setBenchCountry(
//             "Brussels"
//         );
//         location.setBenchSize(
//             "Large"
//         );
//         location.setBenchType(
//             "Public"
//         );
//         location.setConnectedRoutes(
//             3
//         );

//         /**
//          * 1L, "Park Bench", "City Council", "Park Street", 
//             "Brussels", "Belgium", "Large", "Public", 3, 
//             null, null, null, null, null, null, null, null,
//             null, null, null, null, null, null, 
//             null, null, null, null, null, null, 
//             null, null, null, null, null, null, 
//             null, 12000, "mixed", "park,free"
//          */

//         locationDTO = new LocationDTO(
//             1L, "Park Bench", "City Council", "Park Street", 
//             "Brussels", "Belgium", "Large", "Public", 3, 
//             "park,free", "photo.jpg", null, null
//         );
//     }

//     @Test
//     void getAllLocationDTOs_ReturnsAllMappedLocations() {
//         // Arrange
//         when(locationRepository.findAll()).thenReturn(Arrays.asList(location));
        
//         // Act
//         List<LocationDTO> result = locationService.getAllLocationDTOs();
        
//         // Assert
//         assertEquals(1, result.size());
//         LocationDTO dto = result.get(0);
//         assertLocationMappedCorrectly(dto);
//         verify(locationRepository, times(1)).findAll();
//     }

//     @Test
//     void getLocationDTOById_ExistingId_ReturnsLocationDTO() {
//         // Arrange
//         when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        
//         // Act
//         Optional<LocationDTO> result = locationService.getLocationDTOById(1L);
        
//         // Assert
//         assertTrue(result.isPresent());
//         assertLocationMappedCorrectly(result.get());
//         verify(locationRepository, times(1)).findById(1L);
//     }

//     @Test
//     void getLocationDTOById_NonExistingId_ReturnsEmpty() {
//         // Arrange
//         when(locationRepository.findById(99L)).thenReturn(Optional.empty());
        
//         // Act
//         Optional<LocationDTO> result = locationService.getLocationDTOById(99L);
        
//         // Assert
//         assertFalse(result.isPresent());
//         verify(locationRepository, times(1)).findById(99L);
//     }

//     @Test
//     void createLocation_ValidLocation_ReturnsSavedLocation() {
//         // Arrange
//         when(locationRepository.save(any(Location.class))).thenReturn(location);
        
//         // Act
//         Location result = locationService.createLocation(location);
        
//         // Assert
//         assertNotNull(result);
//         assertEquals("Park Bench", result.getBenchName());
//         verify(locationRepository, times(1)).save(location);
//     }

//     @Test
//     void updateLocation_ExistingIdAndValidDTO_ReturnsUpdatedLocation() {
//         // Arrange
//         when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
//         when(locationRepository.save(any(Location.class))).thenReturn(location);
        
//         // Create update DTO with partial updates
//         LocationDTO updateDTO = new LocationDTO(
//             null, "Updated Bench Name", null, null, 
//             "Antwerp", null, null, null, 5, 
//             null, null, null, null
//         );
        
//         // Act
//         Location result = locationService.updateLocation(1L, updateDTO);
        
//         // Assert
//         assertEquals("Updated Bench Name", result.getBenchName());
//         assertEquals("Antwerp", result.getBenchCity());
//         assertEquals(3, result.getConnectedRoutes());
//         verify(locationRepository, times(1)).findById(1L);
//         verify(locationRepository, times(1)).save(location);
//     }

//     @Test
//     void updateLocation_ExistingIdPartialNullFields_UpdatesOnlyNonNullFields() {
//         // Setup existing location
//         Location originalLocation = new Location();
//         originalLocation.setBenchId(1L);
//         originalLocation.setBenchName("Original");
//         originalLocation.setBenchCity("Brussels");
//         originalLocation.setConnectedRoutes(3);
        
//         when(locationRepository.findById(1L)).thenReturn(Optional.of(originalLocation));
//         when(locationRepository.save(any(Location.class))).thenReturn(originalLocation);
        
//         // Only update city
//         LocationDTO partialUpdate = new LocationDTO(
//             null, null, null, null, "Antwerp", null, null, null, 0,
//             null, null, null, null
//         );
        
//         // Act
//         Location updated = locationService.updateLocation(1L, partialUpdate);
        
//         // Assert
//         assertEquals("Original", updated.getBenchName()); // Name unchanged
//         assertEquals("Antwerp", updated.getBenchCity()); // City updated
//         assertEquals(3, updated.getConnectedRoutes()); // Routes unchanged
//     }

//     @Test
//     void updateLocation_NonExistingId_ThrowsException() {
//         // Arrange
//         when(locationRepository.findById(99L)).thenReturn(Optional.empty());
        
//         // Act & Assert
//         RuntimeException exception = assertThrows(RuntimeException.class, 
//             () -> locationService.updateLocation(99L, locationDTO));
        
//         assertEquals("Location not found with id: 99", exception.getMessage());
//         verify(locationRepository, times(1)).findById(99L);
//         verify(locationRepository, never()).save(any());
//     }

//     @Test
//     void deleteLocation_ExistingId_DeletesSuccessfully() {
//         // Arrange
//         when(locationRepository.existsById(1L)).thenReturn(true);
        
//         // Act
//         locationService.deleteLocation(1L);
        
//         // Assert
//         verify(locationRepository, times(1)).existsById(1L);
//         verify(locationRepository, times(1)).deleteById(1L);
//     }

//     @Test
//     void deleteLocation_NonExistingId_ThrowsException() {
//         // Arrange
//         when(locationRepository.existsById(99L)).thenReturn(false);
        
//         // Act & Assert
//         RuntimeException exception = assertThrows(RuntimeException.class, 
//             () -> locationService.deleteLocation(99L));
        
//         assertEquals("Location not found with id: 99", exception.getMessage());
//         verify(locationRepository, times(1)).existsById(99L);
//         verify(locationRepository, never()).deleteById(anyLong());
//     }

//     @Test
//     void updateLocation_UpdateAllFields_AllFieldsUpdated() {
//         // Arrange
//         Location original = new Location();
//         original.setBenchId(1L);
//         original.setBenchName("Original");
//         original.setBenchOwner("Original Owner");
//         original.setBenchCity("Brussels");
//         original.setBenchCountry("Belgium");
//         original.setBenchSize("Medium");
//         original.setBenchType("Private");
//         original.setTags("original");

//         when(locationRepository.findById(1L)).thenReturn(Optional.of(original));
//         when(locationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

//         LocationDTO updateDTO = new LocationDTO(
//             1L, 
//             "Updated Bench",
//             "New Owner",
//             "Updated Street",
//             "Antwerp",
//             "Netherlands",
//             "Large",
//             "Public",
//             5,
//             "updated,tags",
//             null,
//             null, null
//         );

//         // Act
//         Location updated = locationService.updateLocation(1L, updateDTO);

//         // Assert
//         assertAll(
//             () -> assertEquals("Updated Bench", updated.getBenchName()),
//             () -> assertEquals("New Owner", updated.getBenchOwner()),
//             () -> assertEquals("Antwerp", updated.getBenchCity()),
//             () -> assertEquals("Netherlands", updated.getBenchCountry()),
//             () -> assertEquals("Large", updated.getBenchSize()),
//             () -> assertEquals("Public", updated.getBenchType()),
//             () -> assertEquals("updated,tags", updated.getTags())
//         );
//     }

//     @Test
//     void updateLocation_CountryUpdate_OnlyCountryChanged() {
//         // Arrange
//         Location original = new Location();
//         original.setBenchId(1L);
//         original.setBenchCountry("Belgium");
        
//         when(locationRepository.findById(1L)).thenReturn(Optional.of(original));
//         when(locationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

//         LocationDTO updateDTO = new LocationDTO(null, null, null, null, null, "France", null, null, 0, null, null, null, null);

//         // Act
//         Location updated = locationService.updateLocation(1L, updateDTO);

//         // Assert
//         assertEquals("France", updated.getBenchCountry());
//     }

//     @Test
//     void updateLocation_SizeAndTypeUpdate_BothFieldsChanged() {
//         // Arrange
//         Location original = new Location();
//         original.setBenchId(1L);
//         original.setBenchSize("Medium");
//         original.setBenchType("Private");
        
//         when(locationRepository.findById(1L)).thenReturn(Optional.of(original));
//         when(locationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

//         LocationDTO updateDTO = new LocationDTO(null, null, null, null, null, null, "Large", "Public", 0, null, null, null, null);

//         // Act
//         Location updated = locationService.updateLocation(1L, updateDTO);

//         // Assert
//         assertAll(
//             () -> assertEquals("Large", updated.getBenchSize()),
//             () -> assertEquals("Public", updated.getBenchType())
//         );
//     }

//     private void assertLocationMappedCorrectly(LocationDTO dto) {
//         assertEquals(location.getBenchId(), dto.getId());
//         assertEquals(location.getBenchName(), dto.getName());
//         assertEquals(location.getBenchOwner(), dto.getOwner());
//         assertEquals(location.getBenchCity(), dto.getCity());
//         assertEquals(location.getBenchCountry(), dto.getCountry());
//         assertEquals(location.getBenchSize(), dto.getSize());
//         assertEquals(location.getBenchType(), dto.getType());
//         assertEquals(location.getConnectedRoutes(), dto.getConnectedRoutes());
//         assertEquals(location.getTags(), dto.getTags());
//     }
// }