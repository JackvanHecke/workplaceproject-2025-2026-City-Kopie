package be.ucll.unit.model;

import be.ucll.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class CoachTest {

    private Coach coach;
    private Profile profile;
    private Location location;
    private CoachOffer offer;
    private CoachAvailability availability;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        coach = new Coach(profile);
        location = new Location();
        offer = new CoachOffer();
        availability = new CoachAvailability();
    }

    @Test
    void testConstructorWithProfile() {
        assertEquals(profile, coach.getProfile());
        assertTrue(coach.getNotifyNewDevices());
        assertEquals(5, coach.getDefaultRadiusKm());
        assertTrue(coach.getIsAvailable());
        assertTrue(coach.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testDefaultConstructor() {
        Coach emptyCoach = new Coach();
        assertNull(emptyCoach.getProfile());
        assertTrue(emptyCoach.getNotifyNewDevices());
        assertEquals(5, emptyCoach.getDefaultRadiusKm());
        assertTrue(emptyCoach.getIsAvailable());
        assertNotNull(emptyCoach.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
        coach.setBio("Certified trainer with 10 years experience");
        coach.setNotifyNewDevices(false);
        coach.setDefaultRadiusKm(10);
        coach.setIsAvailable(false);

        LocalDateTime customTime = LocalDateTime.now().minusDays(1);
        coach.setCreatedAt(customTime);

        assertAll(
            () -> assertEquals(null, coach.getId()),
            () -> assertEquals("Certified trainer with 10 years experience", coach.getBio()),
            () -> assertFalse(coach.getNotifyNewDevices()),
            () -> assertEquals(10, coach.getDefaultRadiusKm()),
            () -> assertFalse(coach.getIsAvailable()),
            () -> assertEquals(customTime, coach.getCreatedAt())
        );
    }

    @Test
    void testLocationManagement() {
        coach.addLocation(location);
        assertTrue(coach.getLocations().contains(location));

        coach.removeLocation(location);
        assertFalse(coach.getLocations().contains(location));
    }

    @Test
    void testAddRemoveOffers() {
        coach.addOffer(offer);
        assertTrue(coach.getOffers().contains(offer));
        assertEquals(coach, offer.getCoach());

        coach.removeOffer(offer);
        assertFalse(coach.getOffers().contains(offer));
        assertNull(offer.getCoach());
    }

    @Test
    void testAddRemoveAvailability() {
        coach.addAvailability(availability);
        assertTrue(coach.getAvailability().contains(availability));
        assertEquals(coach, availability.getCoach());

        coach.removeAvailability(availability);
        assertFalse(coach.getAvailability().contains(availability));
        assertNull(availability.getCoach());
    }

    @Test
    void testSetProfile() {
        Profile newProfile = new Profile();
        coach.setProfile(newProfile);
        assertEquals(newProfile, coach.getProfile());
    }

    @Test
    void testNullFieldHandling() {
        coach.setBio(null);
        coach.setNotifyNewDevices(null);
        coach.setDefaultRadiusKm(null);
        coach.setIsAvailable(null);
        coach.setCreatedAt(null);

        assertAll(
            () -> assertNull(coach.getBio()),
            () -> assertNull(coach.getNotifyNewDevices()),
            () -> assertNull(coach.getDefaultRadiusKm()),
            () -> assertNull(coach.getIsAvailable()),
            () -> assertNull(coach.getCreatedAt())
        );
    }

    @Test
    void testBoundaryRadiusValues() {
        coach.setDefaultRadiusKm(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, coach.getDefaultRadiusKm());

        coach.setDefaultRadiusKm(0);
        assertEquals(0, coach.getDefaultRadiusKm());

        coach.setDefaultRadiusKm(-10);
        assertEquals(-10, coach.getDefaultRadiusKm());
    }

    @Test
    void testSetCollectionDirectly() {
        Set<Location> locations = new HashSet<>();
        locations.add(location);
        coach.setLocations(locations);
        assertEquals(1, coach.getLocations().size());
    }

    @Test
    void testConvenienceMethodsBidirectionalControl() {
        coach.addLocation(location);

        coach.addOffer(offer);
        assertEquals(coach, offer.getCoach());

        coach.addAvailability(availability);
        assertEquals(coach, availability.getCoach());
    }

    @Test
    void testRemoveNonexistentItems() {
        Location newLocation = new Location();
        coach.removeLocation(newLocation); // Should not throw exception
        assertFalse(coach.getLocations().contains(newLocation));
    }

    @Test
    void testEdgeCaseLargeCollections() {
        Set<CoachOffer> offers = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            CoachOffer newOffer = new CoachOffer();
            offers.add(newOffer);
            coach.addOffer(newOffer);
        }
        assertEquals(100, coach.getOffers().size());
    }

    @Test
    void testCreatedAtAutoPopulation() {
        Coach newCoach = new Coach();
        assertTrue(newCoach.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));

        LocalDateTime presetTime = LocalDateTime.now().minusDays(5);
        newCoach.setCreatedAt(presetTime);
        assertEquals(presetTime, newCoach.getCreatedAt());
    }
}