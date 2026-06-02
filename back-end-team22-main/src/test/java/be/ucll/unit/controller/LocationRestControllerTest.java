// package be.ucll.unit.controller;

// import be.ucll.controller.LocationRestController;
// import be.ucll.model.Location;
// import be.ucll.model.dto.LocationDTO;
// import be.ucll.service.LocationService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import java.util.List;
// import java.util.Optional;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class LocationRestControllerTest {

//     @Mock
//     private LocationService locationService;

//     @InjectMocks
//     private LocationRestController controller;

//     private Location location;
//     private LocationDTO locationDTO;

//     @BeforeEach
//     void setUp() {
//         location = new Location();
//         location.setBenchId(1L);
//         location.setBenchName("Central Park Bench");

//         locationDTO = new LocationDTO(
//             1L, "Central Park Bench", "City Parks", null, 
//             "New York", "USA", "Large", "Public", 3, 
//             null, null, null, null);
//     }

//     @Test
//     void getAllLocations_ReturnsListOfLocationDTOs() {
//         // Arrange
//         when(locationService.getAllLocationDTOs()).thenReturn(List.of(locationDTO));

//         // Act
//         List<LocationDTO> response = controller.getAllLocations();

//         // Assert
//         assertEquals(1, response.size());
//         assertEquals("Central Park Bench", response.get(0).getName());
//         verify(locationService, times(1)).getAllLocationDTOs();
//     }

//     @Test
//     void getLocationById_ExistingId_ReturnsLocationDTO() {
//         // Arrange
//         when(locationService.getLocationDTOById(1L)).thenReturn(Optional.of(locationDTO));

//         // Act
//         LocationDTO response = controller.getLocationById(1L);

//         // Assert
//         assertEquals(locationDTO, response);
//         verify(locationService, times(1)).getLocationDTOById(1L);
//     }

//     @Test
//     void getLocationById_NonExistingId_ThrowsException() {
//         // Arrange
//         when(locationService.getLocationDTOById(99L)).thenReturn(Optional.empty());

//         // Act & Assert
//         RuntimeException exception = assertThrows(RuntimeException.class,
//             () -> controller.getLocationById(99L));

//         assertEquals("Location not found with id: 99", exception.getMessage());
//     }

//     @Test
//     void createLocation_ValidInput_ReturnsCreatedDTO() {
//         // Arrange
//         when(locationService.createLocation(any(Location.class))).thenReturn(location);
//         when(locationService.getLocationDTOById(1L)).thenReturn(Optional.of(locationDTO));

//         // Act
//         ResponseEntity<LocationDTO> response = controller.createLocation(location);

//         // Assert
//         assertEquals(HttpStatus.CREATED, response.getStatusCode());
//         assertEquals(locationDTO, response.getBody());
//         verify(locationService, times(1)).createLocation(location);
//     }

//     @Test
//     void updateLocation_ValidIdAndDTO_ReturnsUpdatedDTO() {
//         // Arrange
//         when(locationService.updateLocation(1L, locationDTO)).thenReturn(location);
//         when(locationService.getLocationDTOById(1L)).thenReturn(Optional.of(locationDTO));

//         // Act
//         ResponseEntity<LocationDTO> response = controller.updateLocation(1L, locationDTO);

//         // Assert
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertEquals(locationDTO, response.getBody());
//         verify(locationService, times(1)).updateLocation(1L, locationDTO);
//     }

//     @Test
//     void updateLocation_NonExistingId_ThrowsException() {
//         // Arrange
//         when(locationService.updateLocation(99L, locationDTO))
//             .thenThrow(new RuntimeException("Location not found with id: 99"));

//         assertThrows(RuntimeException.class, () -> controller.updateLocation(99L, locationDTO));
//     }

//     @Test
//     void deleteLocation_ExistingId_ReturnsNoContent() {
//         // Arrange
//         doNothing().when(locationService).deleteLocation(1L);

//         // Act
//         ResponseEntity<Void> response = controller.deleteLocation(1L);

//         // Assert
//         assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//         verify(locationService, times(1)).deleteLocation(1L);
//     }

//     @Test
//     void deleteLocation_NonExistingId_ThrowsException() {
//         // Arrange
//         doThrow(new RuntimeException("Location not found with id: 99"))
//             .when(locationService).deleteLocation(99L);

//         assertThrows(RuntimeException.class, () -> controller.deleteLocation(99L));
//     }

//     @Test
//     void handleDomainException_ReturnsErrorResponse() {
//         // Arrange
//         String errorMessage = "Test error message";
//         RuntimeException ex = new RuntimeException(errorMessage);

//         controller.handleDomainException(ex);
//     }
// }