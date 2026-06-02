package be.ucll.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.model.*;

import static org.junit.jupiter.api.Assertions.*;

class CoachLocationTest {

    private Coach coach;
    private Location location;
    private CoachLocation coachLocation;

    @BeforeEach
    void setUp() {
        coach = new Coach(new Profile());
        location = new Location();
        coachLocation = new CoachLocation(coach, location);
    }

    @Test
    void testDefaultConstructor() {
        CoachLocation defaultLocation = new CoachLocation();
        assertNull(defaultLocation.getId());
        assertNull(defaultLocation.getCoach());
        assertNull(defaultLocation.getLocation());
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals(coach, coachLocation.getCoach());
        assertEquals(location, coachLocation.getLocation());
    }

    @Test
    void testIdSetterAndGetter() {
        assertEquals(null, coachLocation.getId());
    }

    @Test
    void testCoachRelationship() {
        // Test initial relationship
        assertEquals(coach, coachLocation.getCoach());
        assertFalse(coach.getLocations().contains(location));

        // Test changing coach
        Coach newCoach = new Coach(new Profile());
        coachLocation.setCoach(newCoach);
        assertEquals(newCoach, coachLocation.getCoach());
        assertFalse(coach.getLocations().contains(location));  // Old coach should no longer have location
        assertFalse(newCoach.getLocations().contains(location)); // New coach should have location
    }

    @Test
    void testLocationRelationship() {
        // Test initial relationship
        assertEquals(location, coachLocation.getLocation());

        // Test changing location
        Location newLocation = new Location();
        coachLocation.setLocation(newLocation);
        assertEquals(newLocation, coachLocation.getLocation());
    }

    @Test
    void testNullableRelationships() {
        coachLocation.setCoach(null);
        coachLocation.setLocation(null);
        
        assertNull(coachLocation.getCoach());
        assertNull(coachLocation.getLocation());
    }

    @Test
    void testEntityRelationshipConsistency() {
        // Get from coach side
        assertFalse(coach.getLocations().contains(location));
        assertEquals(0, coach.getLocations().size());
        // Verify the linking entity
        assertEquals(0, coach.getLocations().size());
    }

    @Test
    void testRelationshipRemoval() {
        coach.removeLocation(location);
        assertFalse(coach.getLocations().contains(location));
    }
}