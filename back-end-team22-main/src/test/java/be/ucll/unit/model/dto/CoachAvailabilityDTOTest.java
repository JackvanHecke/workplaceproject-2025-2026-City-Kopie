// package be.ucll.unit.model.dto;

// import org.junit.jupiter.api.Test;

// import be.ucll.model.dto.*;

// import java.time.LocalDateTime;
// import static org.junit.jupiter.api.Assertions.*;

// class CoachAvailabilityDTOTest {

//     private static final LocalDateTime NOW = LocalDateTime.now();
//     private static final LocationDTO TEST_BENCH = new LocationDTO(1L, "Main Bench", "Park Center");

//     @Test
//     void testParameterizedConstructorAndGetters_AllFields() {
//         CoachAvailabilityDTO dto = new CoachAvailabilityDTO(
//             1L,
//             NOW,
//             NOW.plusHours(1),
//             "Available for emergency sessions",
//             true,
//             "FREQ=WEEKLY;INTERVAL=1",
//             TEST_BENCH,
//             NOW.minusDays(1)
//         );

//         assertAll(
//             () -> assertEquals(1L, dto.getId()),
//             () -> assertEquals(NOW, dto.getAvailableFrom()),
//             () -> assertEquals(NOW.plusHours(1), dto.getAvailableTo()),
//             () -> assertEquals("Available for emergency sessions", dto.getNote()),
//             () -> assertTrue(dto.getIsRecurring()),
//             () -> assertEquals("FREQ=WEEKLY;INTERVAL=1", dto.getRecurrenceRule()),
//             () -> assertEquals(TEST_BENCH, dto.getBench()),
//             () -> assertEquals(NOW.minusDays(1), dto.getCreatedAt())
//         );
//     }

//     @Test
//     void testConstructorWithNullValues() {
//         CoachAvailabilityDTO dto = new CoachAvailabilityDTO(
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
//             () -> assertNull(dto.getAvailableFrom()),
//             () -> assertNull(dto.getAvailableTo()),
//             () -> assertNull(dto.getNote()),
//             () -> assertNull(dto.getIsRecurring()),
//             () -> assertNull(dto.getRecurrenceRule()),
//             () -> assertNull(dto.getBench()),
//             () -> assertNull(dto.getCreatedAt())
//         );
//     }

//     @Test
//     void testEdgeCase_EmptyStrings() {
//         CoachAvailabilityDTO dto = new CoachAvailabilityDTO(
//             null, null, null, "", false, "", null, null
//         );

//         assertAll(
//             () -> assertEquals("", dto.getNote()),
//             () -> assertEquals("", dto.getRecurrenceRule())
//         );
//     }

//     @Test
//     void testSameDateTimeBounds() {
//         //public CoachAvailabilityDTO(Long id, LocalDateTime availableFrom, LocalDateTime availableTo,
//         //                        String note, Boolean isRecurring, String recurrenceRule,
//         //                        LocationDTO bench, LocalDateTime createdAt)
//         CoachAvailabilityDTO dto = new CoachAvailabilityDTO(
//             null, NOW, NOW, null, null, null, null, null
//         );

//         assertEquals(NOW, dto.getAvailableFrom());
//         assertEquals(NOW, dto.getAvailableTo());
//     }

//     @Test
//     void testBooleanEdgeCases() {
//         CoachAvailabilityDTO trueDto = new CoachAvailabilityDTO(
//             null, null, null, null, true, null, null, null
//         );

//         CoachAvailabilityDTO falseDto = new CoachAvailabilityDTO(
//             null, null, null, null, false, null, null, null
//         );

//         assertTrue(trueDto.getIsRecurring());
//         assertFalse(falseDto.getIsRecurring());
//     }

//     @Test
//     void testLongNoteAndRecurrenceRule() {
//         String longNote = "A".repeat(1000);
//         String longRule = "B".repeat(255);

//         CoachAvailabilityDTO dto = new CoachAvailabilityDTO(
//                         null, NOW, NOW, longNote, null, longRule, null, null
//         );

//         assertEquals(longNote, dto.getNote());
//         assertEquals(longRule, dto.getRecurrenceRule());
//     }

//     @Test
//     void testDifferentBenchSettings() {
//         // Test no bench association
//         CoachAvailabilityDTO nullBenchDto = new CoachAvailabilityDTO(
//             1L, NOW, NOW.plusHours(1), null, null, null, null, NOW
//         );
//         assertNull(nullBenchDto.getBench());

//         // Test minimum location info
//         LocationDTO minimalBench = new LocationDTO(2L, null, null);
//         CoachAvailabilityDTO minimalBenchDto = new CoachAvailabilityDTO(
//             1L, NOW, NOW, "",false,"", minimalBench, NOW
//         );
//         assertEquals(2L, minimalBenchDto.getBench().getId());
//     }
// }