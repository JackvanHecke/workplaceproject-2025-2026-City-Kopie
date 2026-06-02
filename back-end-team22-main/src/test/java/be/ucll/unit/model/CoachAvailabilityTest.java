package be.ucll.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.model.Coach;
import be.ucll.model.CoachAvailability;
import be.ucll.model.Profile;
import be.ucll.model.Location;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CoachAvailabilityTest {

    private CoachAvailability availability;
    private LocalDateTime testStartTime;
    private LocalDateTime testEndTime;

    @BeforeEach
    void setUp() {
        availability = new CoachAvailability();
        testStartTime = LocalDateTime.of(2023, 10, 15, 9, 0);
        testEndTime = LocalDateTime.of(2023, 10, 15, 10, 0);
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(availability);
        assertNull(availability.getCoach());
        assertNull(availability.getBench());
        assertFalse(availability.getIsRecurring());
        assertNull(availability.getRecurrenceRule());
        assertNull(availability.getNote());
        assertNotNull(availability.getCreatedAt());
    }

    @Test
    void testIdSetterAndGetter() {
        assertNull(availability.getId());
    }

    @Test
    void testCoachRelationship() {
        Coach coach = new Coach(new Profile());
        availability.setCoach(coach);
        assertSame(coach, availability.getCoach());
    }

    @Test
    void testBenchSetterAndGetter() {
        Location bench = new Location(null, "Test Bench", null, null, null, null, null, 0, null, null, null, null, null, null, null, null, null, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null);
        availability.setBench(bench);
        assertSame(bench, availability.getBench());

        availability.setBench(null);
        assertNull(availability.getBench());
    }

    @Test
    void testAvailablePeriod() {
        availability.setAvailableFrom(testStartTime);
        availability.setAvailableTo(testEndTime);

        assertEquals(testStartTime, availability.getAvailableFrom());
        assertEquals(testEndTime, availability.getAvailableTo());
    }

    @Test
    void testRecurrenceSettings() {
        availability.setIsRecurring(true);
        availability.setRecurrenceRule("FREQ=WEEKLY;INTERVAL=1;BYDAY=MO");

        assertTrue(availability.getIsRecurring());
        assertEquals("FREQ=WEEKLY;INTERVAL=1;BYDAY=MO", availability.getRecurrenceRule());
    }

    @Test
    void testNoteSetterAndGetter() {
        String testNote = "Available for emergency sessions only";
        availability.setNote(testNote);
        assertEquals(testNote, availability.getNote());
    }

    @Test
    void testCreatedAt() {
        LocalDateTime customTime = LocalDateTime.of(2023, 1, 1, 0, 0);
        availability.setCreatedAt(customTime);
        assertEquals(customTime, availability.getCreatedAt());

        // Test default value
        CoachAvailability newAvailability = new CoachAvailability();
        assertNotNull(newAvailability.getCreatedAt());
        assertTrue(newAvailability.getCreatedAt().isAfter(customTime));
    }

    @Test
    void testFieldDefaultValues() {
        // Test defaults set in entity
        assertFalse(availability.getIsRecurring());
        assertNotNull(availability.getCreatedAt());
    }

    @Test
    void testOverwriteDefaultIsRecurring() {
        availability.setIsRecurring(null);
        assertNull(availability.getIsRecurring());
    }

    // Edge case tests
    @Test
    void testSameTimeForAvailability() {
        LocalDateTime sameTime = LocalDateTime.now();
        availability.setAvailableFrom(sameTime);
        availability.setAvailableTo(sameTime);

        assertEquals(sameTime, availability.getAvailableFrom());
        assertEquals(sameTime, availability.getAvailableTo());
    }

    @Test
    void testLongStrings() {
        String longNote = "A".repeat(1000);
        String longRecurrence = "B".repeat(255);

        availability.setNote(longNote);
        availability.setRecurrenceRule(longRecurrence);

        assertEquals(longNote, availability.getNote());
        assertEquals(longRecurrence, availability.getRecurrenceRule());
    }
    
    @Test
    void testEntityRelationships() {
        Coach coach = new Coach(new Profile());
        Location location = new Location(null, "Main Bench", null, null, null, null, null, 0, null, null, null, null, null, null, null, null, null, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null);
        
        availability.setCoach(coach);
        availability.setBench(location);
        
        // Test bidirectional relationship management
        coach.addAvailability(availability);
        assertSame(coach, availability.getCoach());
        assertTrue(coach.getAvailability().contains(availability));
    }
}
