package be.ucll.unit.model.dto;

import be.ucll.model.dto.MovementDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class MovementDTOTest {

    private final LocalDateTime NOW = LocalDateTime.now();
    private final LocalDateTime MIN_DATE = LocalDateTime.MIN;
    private final LocalDateTime MAX_DATE = LocalDateTime.MAX;

    @Test
    void testDefaultConstructor() {
        MovementDTO dto = new MovementDTO();
        
        assertAll(
            () -> assertNull(dto.getDate()),
            () -> assertNull(dto.getNote()),
            () -> assertNull(dto.getLatitude()),
            () -> assertNull(dto.getLongitude())
        );
    }

    @Test
    void testParameterizedConstructor_ValidValues() {
        MovementDTO dto = new MovementDTO(
            NOW,
            "Relocated to new position",
            50.8503,
            4.3517
        );

        assertAll(
            () -> assertEquals(NOW, dto.getDate()),
            () -> assertEquals("Relocated to new position", dto.getNote()),
            () -> assertEquals(50.8503, dto.getLatitude()),
            () -> assertEquals(4.3517, dto.getLongitude())
        );
    }

    @Test
    void testNullValues() {
        MovementDTO dto = new MovementDTO(
            null,
            null,
            null,
            null
        );

        assertAll(
            () -> assertNull(dto.getDate()),
            () -> assertNull(dto.getNote()),
            () -> assertNull(dto.getLatitude()),
            () -> assertNull(dto.getLongitude())
        );
    }

    @Test
    void testDateTimeBoundaries() {
        MovementDTO minDto = new MovementDTO(MIN_DATE, null, null, null);
        MovementDTO maxDto = new MovementDTO(MAX_DATE, null, null, null);

        assertAll(
            () -> assertEquals(MIN_DATE, minDto.getDate()),
            () -> assertEquals(MAX_DATE, maxDto.getDate())
        );
    }

    @Test
    void testCoordinateEdgeCases() {
        MovementDTO dto = new MovementDTO(
            NOW,
            "Extreme coordinates",
            -90.0,
            180.0
        );

        assertAll(
            () -> assertEquals(-90.0, dto.getLatitude()),
            () -> assertEquals(180.0, dto.getLongitude())
        );
    }

    @Test
    void testLongNote() {
        String longNote = "A".repeat(1000);
        MovementDTO dto = new MovementDTO(
            NOW,
            longNote,
            0.0,
            0.0
        );

        assertEquals(longNote, dto.getNote());
    }

    @Test
    void testEmptyNote() {
        MovementDTO dto = new MovementDTO(
            NOW,
            "",
            0.0,
            0.0
        );

        assertEquals("", dto.getNote());
    }

    @Test
    void testPrecisionCoordinates() {
        MovementDTO dto = new MovementDTO(
            NOW,
            "High precision location",
            40.71277621823847,
            -74.005972940347
        );

        assertAll(
            () -> assertEquals(40.71277621823847, dto.getLatitude()),
            () -> assertEquals(-74.005972940347, dto.getLongitude())
        );
    }

    @Test
    void testOutOfBoundsCoordinates() {
        MovementDTO dto = new MovementDTO(
            NOW,
            "Invalid coordinates",
            200.0,
            -190.0
        );

        assertAll(
            () -> assertEquals(200.0, dto.getLatitude()),
            () -> assertEquals(-190.0, dto.getLongitude())
        );
    }
}