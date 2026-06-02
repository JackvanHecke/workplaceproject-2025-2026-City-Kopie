package be.ucll.unit.model.dto;

import org.junit.jupiter.api.Test;

import be.ucll.model.dto.*;

import static org.junit.jupiter.api.Assertions.*;

class CoachLocationDTOTest {

    @Test
    void testValidConstructorAndGetters() {
        CoachLocationDTO dto = new CoachLocationDTO(
            1L,
            "Central Park Bench",
            "New York"
        );

        assertAll(
            () -> assertEquals(1L, dto.getId()),
            () -> assertEquals("Central Park Bench", dto.getBenchName()),
            () -> assertEquals("New York", dto.getBenchCity())
        );
    }

    @Test
    void testNullValues() {
        CoachLocationDTO dto = new CoachLocationDTO(
            null,
            null,
            null
        );

        assertAll(
            () -> assertNull(dto.getId()),
            () -> assertNull(dto.getBenchName()),
            () -> assertNull(dto.getBenchCity())
        );
    }

    @Test
    void testEdgeCaseStrings() {
        String longName = "A".repeat(255);
        String longCity = "B".repeat(100);

        CoachLocationDTO dto = new CoachLocationDTO(
            2L,
            longName,
            longCity
        );

        assertAll(
            () -> assertEquals(longName, dto.getBenchName()),
            () -> assertEquals(longCity, dto.getBenchCity())
        );
    }

    @Test
    void testEmptyStrings() {
        CoachLocationDTO dto = new CoachLocationDTO(
            3L,
            "",
            ""
        );

        assertAll(
            () -> assertEquals("", dto.getBenchName()),
            () -> assertEquals("", dto.getBenchCity())
        );
    }

    @Test
    void testMixedNullAndValidValues() {
        CoachLocationDTO dto1 = new CoachLocationDTO(
            null,
            "Valid Bench",
            null
        );

        CoachLocationDTO dto2 = new CoachLocationDTO(
            4L,
            null,
            "Valid City"
        );

        assertAll(
            () -> assertNull(dto1.getId()),
            () -> assertEquals("Valid Bench", dto1.getBenchName()),
            () -> assertNull(dto1.getBenchCity()),

            () -> assertEquals(4L, dto2.getId()),
            () -> assertNull(dto2.getBenchName()),
            () -> assertEquals("Valid City", dto2.getBenchCity())
        );
    }

    @Test
    void testIdBoundaryValues() {
        CoachLocationDTO maxId = new CoachLocationDTO(
            Long.MAX_VALUE,
            "Bench",
            "City"
        );
        CoachLocationDTO minId = new CoachLocationDTO(
            Long.MIN_VALUE,
            "Bench",
            "City"
        );
        CoachLocationDTO zeroId = new CoachLocationDTO(
            0L,
            "Bench",
            "City"
        );

        assertAll(
            () -> assertEquals(Long.MAX_VALUE, maxId.getId()),
            () -> assertEquals(Long.MIN_VALUE, minId.getId()),
            () -> assertEquals(0L, zeroId.getId())
        );
    }

    @Test
    void testSpecialCharacters() {
        CoachLocationDTO dto = new CoachLocationDTO(
            5L,
            "Bench_123!@# 你好",
            "City_456$%^ こんにちは"
        );

        assertAll(
            () -> assertEquals("Bench_123!@# 你好", dto.getBenchName()),
            () -> assertEquals("City_456$%^ こんにちは", dto.getBenchCity())
        );
    }
}