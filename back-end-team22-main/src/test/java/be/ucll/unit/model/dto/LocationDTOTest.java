// package be.ucll.unit.model.dto;

// import org.junit.jupiter.api.Test;

// import be.ucll.model.dto.*;

// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;
// import static org.junit.jupiter.api.Assertions.*;

// class LocationDTOTest {

//     private final MovementDTO MOVEMENT_1 = new MovementDTO();
//     private final MovementDTO MOVEMENT_2 = new MovementDTO();
//     private final List<MovementDTO> MOVEMENT_HISTORY = Arrays.asList(MOVEMENT_1, MOVEMENT_2);

//     @Test
//     void testFullConstructorWithAllFields() {
//         LocationDTO dto = new LocationDTO(
//             1L,
//             "Central Park Bench",
//             "City Parks Dept",
//             "Park Drive East",
//             "New York",
//             "USA",
//             "Large",
//             "Public",
//             3,
//             "wooden,accessible,picnic-area",
//             "https://example.com/bench.jpg",
//             MOVEMENT_HISTORY, null
//         );

//         assertAll(
//             () -> assertEquals(1L, dto.getId()),
//             () -> assertEquals("Central Park Bench", dto.getName()),
//             () -> assertEquals("City Parks Dept", dto.getOwner()),
//             () -> assertEquals("Park Drive East", dto.getStreet()),
//             () -> assertEquals("New York", dto.getCity()),
//             () -> assertEquals("USA", dto.getCountry()),
//             () -> assertEquals("Large", dto.getSize()),
//             () -> assertEquals("Public", dto.getType()),
//             () -> assertEquals(3, dto.getConnectedRoutes()),
//             () -> assertEquals("wooden,accessible,picnic-area", dto.getTags()),
//             () -> assertEquals("https://example.com/bench.jpg", dto.getPhotoUrl()),
//             () -> assertEquals(2, dto.getMovementHistory().size()),
//             () -> assertEquals(MOVEMENT_HISTORY, dto.getMovementHistory())
//         );
//     }

//     @Test
//     void testMinimalConstructor() {
//         LocationDTO dto = new LocationDTO(2L, "Riverside Bench", "London");

//         assertAll(
//             () -> assertEquals(2L, dto.getId()),
//             () -> assertEquals("Riverside Bench", dto.getName()),
//             () -> assertEquals("London", dto.getCity()),
//             () -> assertNull(dto.getOwner()),
//             () -> assertNull(dto.getStreet()),
//             () -> assertNull(dto.getCountry())
//         );
//     }

//     @Test
//     void testEmptyConstructor() {
//         LocationDTO dto = new LocationDTO();
        
//         assertAll(
//             () -> assertNull(dto.getId()),
//             () -> assertNull(dto.getName()),
//             () -> assertNull(dto.getCity())
//         );
//     }

//     @Test
//     void testNullValues() {
//         LocationDTO dto = new LocationDTO(
//             null,
//             null,
//             null,
//             null,
//             null,
//             null,
//             null,
//             null,
//             0,
//             null,
//             null,
//             null, null
//         );

//         assertAll(
//             () -> assertNull(dto.getId()),
//             () -> assertNull(dto.getName()),
//             () -> assertNull(dto.getOwner()),
//             () -> assertEquals(0, dto.getConnectedRoutes())
//         );
//     }

//     @Test
//     void testStringEdgeCases() {
//         String longString = "A".repeat(255);

//         LocationDTO dto = new LocationDTO(
//             null,
//             longString,
//             longString,
//             "",
//             null,
//             null,
//             null,
//             null,
//             Integer.MAX_VALUE,
//             null,
//             null,
//             null, null
//         );

//         assertAll(
//             () -> assertEquals(longString, dto.getName()),
//             () -> assertEquals(longString, dto.getOwner()),
//             () -> assertEquals(null, dto.getCity()),
//             () -> assertEquals(Integer.MAX_VALUE, dto.getConnectedRoutes())
//         );
//     }

//     @Test
//     void testEmptyStrings() {
//         LocationDTO dto = new LocationDTO(
//             4L,
//             "",
//             "",
//             "",
//             "",
//             "",
//             "",
//             "",
//             -1,
//             "",
//             "",
//             MOVEMENT_HISTORY, null
//         );

//         assertAll(
//             () -> assertEquals("", dto.getName()),
//             () -> assertEquals("", dto.getTags()),
//             () -> assertEquals(-1, dto.getConnectedRoutes())
//         );
//     }

//     @Test
//     void testNegativeConnectedRoutes() {
//         LocationDTO dto = new LocationDTO(
//             5L, "Test Bench", null, null, "Test City", null, null, null, -5, null, null, null, null
//         );

//         assertEquals(-5, dto.getConnectedRoutes());
//     }

//     @Test
//     void testMovementHistoryVariations() {
//         @SuppressWarnings("unused")
//         LocationDTO emptyMovement = new LocationDTO(null, null, null);
//         LocationDTO nullMovement = new LocationDTO(null, null, null, null, null, null, null, null, 0, null, null, null, null);
//         @SuppressWarnings("unused")
//         LocationDTO singleMovement = new LocationDTO(null, null, null, null, null, null, null, null, 0, null, null, Collections.singletonList(MOVEMENT_1), null);

//         assertAll(
//             () -> assertNull(nullMovement.getMovementHistory())
//         );
//     }

//     @Test
//     void testTagsFormatting() {
//         LocationDTO dto = new LocationDTO(
//             null, null, null, null, null, null, null, null, 0, "  tag1 , tag2,tag3  ", null, null, null
//         );

//         assertEquals("  tag1 , tag2,tag3  ", dto.getTags());
//     }

//     @Test
//     void testIDBoundaryValues() {
//         LocationDTO maxId = new LocationDTO(Long.MAX_VALUE, "Max Bench", "City");
//         LocationDTO minId = new LocationDTO(Long.MIN_VALUE, "Min Bench", "City");
//         LocationDTO zeroId = new LocationDTO(0L, "Zero Bench", "City");

//         assertAll(
//             () -> assertEquals(Long.MAX_VALUE, maxId.getId()),
//             () -> assertEquals(Long.MIN_VALUE, minId.getId()),
//             () -> assertEquals(0L, zeroId.getId())
//         );
//     }

//     @Test
//     void testPhotoUrlSpecialCases() {
//         LocationDTO dto1 = new LocationDTO(null, null, null, null, null, null, null, null, 0, null, "invalidurl", null, null);
//         LocationDTO dto2 = new LocationDTO(null, null, null, null, null, null, null, null, 0, null, "  whitespace  ", null, null);

//         assertAll(
//             () -> assertEquals("invalidurl", dto1.getPhotoUrl()),
//             () -> assertEquals("  whitespace  ", dto2.getPhotoUrl())
//         );
//     }

//     @Test
//     void testGetCompletedChecklistItems()
//     {
//         LocationDTO dto = new LocationDTO(null, null, null, null, null, null, null, null, 0, null, null, null, null);
//         assertNull(dto.getCompletedChecklistItems());
//     }
// }
