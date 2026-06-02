// package be.ucll.unit.model.dto;

// import org.junit.jupiter.api.Test;

// import be.ucll.model.dto.*;

// import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;

// class CoachDTOTest {

//     private final LocalDateTime NOW = LocalDateTime.now();
//     private final LocationDTO LOCATION = new LocationDTO(1L, "City Park", "Main training area");
//     private final CoachOfferDTO OFFER = new CoachOfferDTO(1L, "Workshop", "Beginners", "Introduction session", null, null, null, NOW, NOW, NOW);
//     private final CoachAvailabilityDTO AVAILABILITY = new CoachAvailabilityDTO(
//         1L, NOW, NOW.plusHours(2), null, false, null, null, NOW);

//     @Test
//     void testFullConstructorWithAllFields() {
//         CoachDTO dto = new CoachDTO(
//             1L,
//             "Certified trainer with 10 years experience",
//             "John Doe",
//             true,
//             NOW.minusDays(30),
//             Arrays.asList(OFFER),
//             Arrays.asList(AVAILABILITY),
//             Arrays.asList(LOCATION)
//         );

//         assertAll(
//             () -> assertEquals(1L, dto.getId()),
//             () -> assertEquals("Certified trainer with 10 years experience", dto.getBio()),
//             () -> assertEquals("John Doe", dto.getName()),
//             () -> assertTrue(dto.getIsAvailable()),
//             () -> assertEquals(NOW.minusDays(30), dto.getCreatedAt()),
//             () -> assertEquals(1, dto.getOffers().size()),
//             () -> assertEquals(OFFER, dto.getOffers().get(0)),
//             () -> assertEquals(1, dto.getAvailability().size()),
//             () -> assertEquals(AVAILABILITY, dto.getAvailability().get(0)),
//             () -> assertEquals(1, dto.getLocations().size()),
//             () -> assertEquals(LOCATION, dto.getLocations().get(0))
//         );
//     }

//     @Test
//     void testPartialNullFields() {
//         CoachDTO dto = new CoachDTO(
//             null,
//             null,
//             null,
//             null,
//             null,
//             null,
//             null,
//             null
//         );

//         assertAll(
//             () -> assertNull(dto.getId()),
//             () -> assertNull(dto.getBio()),
//             () -> assertNull(dto.getName()),
//             () -> assertNull(dto.getIsAvailable()),
//             () -> assertNull(dto.getCreatedAt()),
//             () -> assertNull(dto.getOffers()),
//             () -> assertNull(dto.getAvailability()),
//             () -> assertNull(dto.getLocations())
//         );
//     }

//     @Test
//     void testEmptyCollections() {
//         CoachDTO dto = new CoachDTO(
//             2L,
//             "New coach profile",
//             "Jane Smith",
//             false,
//             NOW,
//             Collections.emptyList(),
//             Collections.emptyList(),
//             Collections.emptyList()
//         );

//         assertAll(
//             () -> assertEquals(2L, dto.getId()),
//             () -> assertTrue(dto.getOffers().isEmpty()),
//             () -> assertTrue(dto.getAvailability().isEmpty()),
//             () -> assertTrue(dto.getLocations().isEmpty())
//         );
//     }

//     @Test
//     void testBooleanVariations() {
//         CoachDTO trueDto = new CoachDTO(null, null, null, true, null, null, null, null);
//         CoachDTO falseDto = new CoachDTO(null, null, null, false, null, null, null, null);
//         CoachDTO nullDto = new CoachDTO(null, null, null, null, null, null, null, null);

//         assertAll(
//             () -> assertTrue(trueDto.getIsAvailable()),
//             () -> assertFalse(falseDto.getIsAvailable()),
//             () -> assertNull(nullDto.getIsAvailable())
//         );
//     }

//     @Test
//     void testStringEdgeCases() {
//         CoachDTO dto = new CoachDTO(
//             3L,
//             "",
//             "A".repeat(255),
//             null,
//             null,
//             null,
//             null,
//             null
//         );

//         assertAll(
//             () -> assertEquals("", dto.getBio()),
//             () -> assertEquals("A".repeat(255), dto.getName())
//         );
//     }

//     @Test
//     void testDateTimeBoundaries() {
//         LocalDateTime minDate = LocalDateTime.MIN;
//         LocalDateTime maxDate = LocalDateTime.MAX;

//         CoachDTO dto = new CoachDTO(
//             null,
//             null,
//             null,
//             null,
//             minDate,
//             null,
//             null,
//             null
//         );

//         assertEquals(minDate, dto.getCreatedAt());

//         // For MAX date - Some systems might have issues with MAX date
//         assertDoesNotThrow(() -> 
//             new CoachDTO(null, null, null, null, maxDate, null, null, null)
//         );
//     }

//     @Test
//     void testCollectionImmutability() {
//         List<CoachOfferDTO> mutableOffers = new java.util.ArrayList<>();
//         mutableOffers.add(OFFER);

//         CoachDTO dto = new CoachDTO(
//             null, null, null, null, null, 
//             mutableOffers, 
//             null, 
//             null
//         );

//         mutableOffers.clear();
//         assertEquals(0, dto.getOffers().size());
//     }

//     @Test
//     void verifyListContents() {
//         CoachDTO dto = new CoachDTO(
//             4L,
//             null,
//             null,
//             null,
//             null,
//             Arrays.asList(OFFER, new CoachOfferDTO(2L, "Private", "Advanced", "1-on-1", null, null, null, NOW, NOW, NOW)),
//             Arrays.asList(AVAILABILITY),
//             Arrays.asList(LOCATION, new LocationDTO(2L, "Beach Side", null))
//         );

//         assertAll(
//             () -> assertEquals(2, dto.getOffers().size()),
//             () -> assertEquals(1, dto.getAvailability().size()),
//             () -> assertEquals(2, dto.getLocations().size())
//         );
//     }
// }