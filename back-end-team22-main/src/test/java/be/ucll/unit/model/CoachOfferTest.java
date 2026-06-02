package be.ucll.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import static org.junit.jupiter.api.Assertions.*;
import be.ucll.model.*;

class CoachOfferTest {

    private Coach coach;
    private CoachOffer offer;
    private Location bench;

    @BeforeEach
    void setUp() {
        coach = new Coach(new Profile());
        bench = new Location();
        offer = new CoachOffer();
        offer.setCoach(coach);
        offer.setBench(bench);
    }

    @Test
    void testDefaultConstructor() {
        CoachOffer defaultOffer = new CoachOffer();
        assertNull(defaultOffer.getId());
        assertNull(defaultOffer.getCoach());
        assertNull(defaultOffer.getBench());
        assertEquals(CoachOffer.FeeType.paid, defaultOffer.getFreeOrPaid());
        assertNotNull(defaultOffer.getCreatedAt());
    }

    @Test
    void testIdSetterAndGetter() {
        assertEquals(null, offer.getId());
    }

    @Test
    void testCoachRelationship() {
        // Test initial setup
        assertEquals(coach, offer.getCoach());
        assertFalse(coach.getOffers().contains(offer));

        // Test changing coach
        Coach newCoach = new Coach(new Profile());
        offer.setCoach(newCoach);
        assertEquals(newCoach, offer.getCoach());
        assertFalse(coach.getOffers().contains(offer));
        assertFalse(newCoach.getOffers().contains(offer));
    }

    @Test
    void testBenchRelationship() {
        // Test bench association
        assertEquals(bench, offer.getBench());
        assertFalse(coach.getLocations().contains(bench));

        // Test changing bench
        Location newBench = new Location();
        offer.setBench(newBench);
        assertEquals(newBench, offer.getBench());
        assertFalse(coach.getLocations().contains(newBench));
    }

    @Test
    void testNullableBench() {
        offer.setBench(null);
        assertNull(offer.getBench());
        
        CoachOffer nullBenchOffer = new CoachOffer();
        nullBenchOffer.setCoach(coach);
        nullBenchOffer.setBench(null);
        assertNull(nullBenchOffer.getBench());
    }

    // Test all property fields
    @Test
    void testOfferProperties() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = start.plusHours(2);
        
        offer.setOfferType("Workshop");
        offer.setTargetGroup("Beginners");
        offer.setDescription("3-hour intensive training session");
        offer.setFreeOrPaid(CoachOffer.FeeType.free);
        offer.setPrice(29.99);
        offer.setRecurrence("Weekly");
        offer.setStartDatetime(start);
        offer.setEndDatetime(end);
        offer.setCreatedAt(now);

        assertEquals("Workshop", offer.getOfferType());
        assertEquals("Beginners", offer.getTargetGroup());
        assertEquals("3-hour intensive training session", offer.getDescription());
        assertEquals(CoachOffer.FeeType.free, offer.getFreeOrPaid());
        assertEquals(29.99, offer.getPrice(), 0.001);
        assertEquals("Weekly", offer.getRecurrence());
        assertEquals(start, offer.getStartDatetime());
        assertEquals(end, offer.getEndDatetime());
        assertEquals(now, offer.getCreatedAt());
    }

    @Test
    void testFeeTypeEnum() {
        offer.setFreeOrPaid(CoachOffer.FeeType.free);
        assertEquals(CoachOffer.FeeType.free, offer.getFreeOrPaid());

        offer.setFreeOrPaid(CoachOffer.FeeType.paid);
        assertEquals(CoachOffer.FeeType.paid, offer.getFreeOrPaid());
    }

    @Test
    void testDefaultFeeType() {
        CoachOffer newOffer = new CoachOffer();
        assertEquals(CoachOffer.FeeType.paid, newOffer.getFreeOrPaid());
    }

    @Test
    void testCreatedAtDefaultsToNow() {
        LocalDateTime beforeCreation = LocalDateTime.now().minusSeconds(1);
        CoachOffer newOffer = new CoachOffer();
        LocalDateTime afterCreation = LocalDateTime.now().plusSeconds(1);

        assertTrue(newOffer.getCreatedAt().isAfter(beforeCreation));
        assertTrue(newOffer.getCreatedAt().isBefore(afterCreation));
    }

    @Test
    void testDateTimeEdgeCases() {
        // Same datetime
        LocalDateTime sameTime = LocalDateTime.now();
        offer.setStartDatetime(sameTime);
        offer.setEndDatetime(sameTime);
        assertEquals(sameTime, offer.getStartDatetime());
        assertEquals(sameTime, offer.getEndDatetime());

        // Null values
        offer.setStartDatetime(null);
        offer.setEndDatetime(null);
        assertNull(offer.getStartDatetime());
        assertNull(offer.getEndDatetime());
    }

    @Test
    void testTimeSensitiveOperations() {
        LocalDateTime start = LocalDateTime.of(2023, 12, 25, 10, 0);
        LocalDateTime end = start.plusHours(3);

        offer.setStartDatetime(start);
        offer.setEndDatetime(end);

        assertEquals(3, ChronoUnit.HOURS.between(offer.getStartDatetime(), offer.getEndDatetime()));
    }

    @Test
    void testLongStrings() {
        String longDescription = "A".repeat(1000);
        String longRecurrence = "B".repeat(255);
        
        offer.setDescription(longDescription);
        offer.setRecurrence(longRecurrence);
        offer.setOfferType("C".repeat(255));
        offer.setTargetGroup("D".repeat(255));

        assertEquals(longDescription, offer.getDescription());
        assertEquals(longRecurrence, offer.getRecurrence());
    }

    @Test
    void testNullProperties() {
        offer.setOfferType(null);
        offer.setTargetGroup(null);
        offer.setDescription(null);
        offer.setRecurrence(null);
        offer.setPrice(null);

        assertNull(offer.getOfferType());
        assertNull(offer.getTargetGroup());
        assertNull(offer.getDescription());
        assertNull(offer.getRecurrence());
        assertNull(offer.getPrice());
    }

    @Test
    void testDecimalPricePrecision() {
        offer.setPrice(19.9999);
        assertEquals(19.9999, offer.getPrice(), 0.00001);
    }

    @Test
    void testNegativePrice() {
        offer.setPrice(-10.0);
        assertEquals(-10.0, offer.getPrice());
    }
}