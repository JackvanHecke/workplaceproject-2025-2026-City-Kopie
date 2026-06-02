package be.ucll.unit.model.dto;

import org.junit.jupiter.api.Test;

import be.ucll.model.dto.*;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CoachOfferDTOTest {

    private final LocalDateTime NOW = LocalDateTime.now();
    private final LocalDateTime START = NOW.plusDays(1);
    private final LocalDateTime END = START.plusHours(2);

    @Test
    void testFullConstructorAndGetters() {
        CoachOfferDTO dto = new CoachOfferDTO(
            1L,
            "Workshop",
            "Beginners",
            "3-hour training session",
            "paid",
            49.99,
            "weekly",
            START,
            END,
            NOW.minusDays(7)
        );

        assertAll(
            () -> assertEquals(1L, dto.getId()),
            () -> assertEquals("Workshop", dto.getOfferType()),
            () -> assertEquals("Beginners", dto.getTargetGroup()),
            () -> assertEquals("3-hour training session", dto.getDescription()),
            () -> assertEquals("paid", dto.getFreeOrPaid()),
            () -> assertEquals(49.99, dto.getPrice()),
            () -> assertEquals("weekly", dto.getRecurrence()),
            () -> assertEquals(START, dto.getStartDatetime()),
            () -> assertEquals(END, dto.getEndDatetime()),
            () -> assertEquals(NOW.minusDays(7), dto.getCreatedAt())
        );
    }

    @Test
    void testNullValues() {
        CoachOfferDTO dto = new CoachOfferDTO(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        assertAll(
            () -> assertNull(dto.getId()),
            () -> assertNull(dto.getOfferType()),
            () -> assertNull(dto.getPrice()),
            () -> assertNull(dto.getStartDatetime())
        );
    }

    @Test
    void testEdgeCaseStrings() {
        String longValue = "A".repeat(1000);
        CoachOfferDTO dto = new CoachOfferDTO(
            2L,
            longValue,
            longValue,
            longValue,
            "free",
            null,
            longValue,
            null,
            null,
            NOW
        );

        assertAll(
            () -> assertEquals(longValue, dto.getOfferType()),
            () -> assertEquals(longValue, dto.getDescription()),
            () -> assertEquals("free", dto.getFreeOrPaid()),
            () -> assertNull(dto.getPrice())
        );
    }

    @Test
    void testEmptyStrings() {
        CoachOfferDTO dto = new CoachOfferDTO(
            3L,
            "",
            "",
            "",
            null,
            0.0,
            "",
            NOW,
            NOW,
            NOW
        );

        assertAll(
            () -> assertEquals("", dto.getOfferType()),
            () -> assertEquals("", dto.getTargetGroup()),
            () -> assertEquals(0.0, dto.getPrice())
        );
    }

    @Test
    void testPriceBoundaryValues() {
        CoachOfferDTO maxPrice = new CoachOfferDTO(
            null, null, null, null, "paid", Double.MAX_VALUE, null, null, null, null);
        CoachOfferDTO minPrice = new CoachOfferDTO(
            null, null, null, null, "paid", Double.MIN_VALUE, null, null, null, null);
        CoachOfferDTO negativePrice = new CoachOfferDTO(
            null, null, null, null, "paid", -19.99, null, null, null, null);
        CoachOfferDTO zeroPrice = new CoachOfferDTO(
            null, null, null, null, "free", 0.0, null, null, null, null);

        assertAll(
            () -> assertEquals(Double.MAX_VALUE, maxPrice.getPrice()),
            () -> assertEquals(Double.MIN_VALUE, minPrice.getPrice()),
            () -> assertEquals(-19.99, negativePrice.getPrice()),
            () -> assertEquals(0.0, zeroPrice.getPrice())
        );
    }

    @Test
    void testDateTimeBehaviour() {
        LocalDateTime minDate = LocalDateTime.MIN;
        LocalDateTime maxDate = LocalDateTime.MAX;

        CoachOfferDTO dto = new CoachOfferDTO(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            minDate,
            maxDate,
            minDate
        );

        assertAll(
            () -> assertEquals(minDate, dto.getStartDatetime()),
            () -> assertEquals(maxDate, dto.getEndDatetime()),
            () -> assertEquals(minDate, dto.getCreatedAt())
        );
    }

    @Test
    void testDifferentFeeTypes() {
        CoachOfferDTO paid = new CoachOfferDTO(
            null, null, null, null, "paid", 1.0, null, null, null, null);
        CoachOfferDTO free = new CoachOfferDTO(
            null, null, null, null, "free", 0.0, null, null, null, null);
        CoachOfferDTO nullFee = new CoachOfferDTO(
            null, null, null, null, null, null, null, null, null, null);

        assertAll(
            () -> assertEquals("paid", paid.getFreeOrPaid()),
            () -> assertEquals("free", free.getFreeOrPaid()),
            () -> assertNull(nullFee.getFreeOrPaid())
        );
    }

    @Test
    void testRecurrenceFormats() {
        CoachOfferDTO cron = new CoachOfferDTO(
            null, null, null, null, null, null, "0 0 12 * * ?", null, null, null);
        CoachOfferDTO rrule = new CoachOfferDTO(
            null, null, null, null, null, null, "FREQ=DAILY;COUNT=10", null, null, null);
        
        assertEquals("0 0 12 * * ?", cron.getRecurrence());
        assertEquals("FREQ=DAILY;COUNT=10", rrule.getRecurrence());
    }

    @Test
    void testIDBoundaryCases() {
        CoachOfferDTO maxId = new CoachOfferDTO(
            Long.MAX_VALUE, null, null, null, null, null, null, null, null, null);
        CoachOfferDTO minId = new CoachOfferDTO(
            Long.MIN_VALUE, null, null, null, null, null, null, null, null, null);
        
        assertEquals(Long.MAX_VALUE, maxId.getId());
        assertEquals(Long.MIN_VALUE, minId.getId());
    }
}