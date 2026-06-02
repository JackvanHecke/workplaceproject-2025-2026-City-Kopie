package be.ucll.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.model.*;
import be.ucll.model.enums.ProfileRole;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    private Profile profile;
    private final LocalDateTime testTime = LocalDateTime.of(2023, 10, 15, 10, 0);

    @BeforeEach
    void setUp() {
        profile = new Profile(
            "John Doe", 
            30, 
            "john@example.com", 
            "male", 
            "password123", 
            "Belgium",
            new BigDecimal("24.5"),
            50, 
            20, 
            5, 
            4500, 
            "saltValue", 
            "hashedPass123"
        );
    }

    @Test
    void testDefaultConstructor() {
        Profile emptyProfile = new Profile();
        
        assertNull(emptyProfile.getId());
        assertNull(emptyProfile.getName());
        assertEquals(0, emptyProfile.getAge());
        assertTrue(emptyProfile.getNotificationsEnabled());
    }

    @Test
    void testParameterizedConstructorWithTimestamp() {
        Profile customProfile = new Profile(
            "Jane Smith", 25, "jane@test.com", "female", "pass", 
            "Netherlands", new BigDecimal("22.0"), 0, 0, 0, 0, 
            testTime, "salt", "hash");
        
        assertEquals("Jane Smith", customProfile.getName());
        assertEquals(testTime, customProfile.getRegisteredAt());
    }

    @Test
    void testParameterizedConstructorWithoutTimestamp() {
        LocalDateTime beforeCreation = LocalDateTime.now().minusSeconds(1);
        Profile timeProfile = new Profile(
            "Time Test", 30, "time@test.com", null, null, 
            null, null, 0, 0, 0, 0, "salt", "hash");
        LocalDateTime afterCreation = LocalDateTime.now().plusSeconds(1);
        
        assertTrue(timeProfile.getRegisteredAt().isAfter(beforeCreation));
        assertTrue(timeProfile.getRegisteredAt().isBefore(afterCreation));
    }

    @Test
    void testAllGettersAndSetters() {
        // ID
        profile.setId(1L);
        assertEquals(1L, profile.getId());
        
        // Name
        profile.setName("Updated Name");
        assertEquals("Updated Name", profile.getName());
        
        // Numeric fields
        profile.setPerformedExercises(-10);
        assertEquals(-10, profile.getPerformedExercises());
        
        // Boolean field
        profile.setNotificationsEnabled(false);
        assertFalse(profile.getNotificationsEnabled());
        
        // Enum field
        profile.setRole(ProfileRole.ADMIN);
        assertEquals(ProfileRole.ADMIN, profile.getRole());
    }

    @Test
    void testDefaultValues() {
        Profile newProfile = new Profile();
        assertTrue(newProfile.getNotificationsEnabled());
        assertEquals(ProfileRole.END_USER, newProfile.getRole());
    }

    @Test
    void testBMIPrecision() {
        BigDecimal preciseBMI = new BigDecimal("28.1234");
        profile.setBmi(preciseBMI);
        assertEquals(0, preciseBMI.compareTo(profile.getBmi()));
    }

    @Test
    void testCollectionsInitialization() {
        Profile newProfile = new Profile();
        
        assertNotNull(newProfile.getValidTokens());
        assertTrue(newProfile.getValidTokens().isEmpty());
        assertNotNull(newProfile.getInboxItems());
        assertTrue(newProfile.getInboxItems().isEmpty());
    }

    @Test
    void testTokenManagement() {
        AuthToken token1 = new AuthToken("token1", null);
        AuthToken token2 = new AuthToken("token2", null);
        
        profile.addValidToken(token1);
        profile.addValidToken(token2);
        assertEquals(2, profile.getValidTokens().size());
        
        profile.removeValidToken(token1);
        assertEquals(1, profile.getValidTokens().size());
    }

    @Test
    void testInboxItemsManagement() {
        ProfileMessage msg1 = new ProfileMessage();
        ProfileMessage msg2 = new ProfileMessage();
        
        profile.getInboxItems().add(msg1);
        profile.getInboxItems().add(msg2);
        assertEquals(2, profile.getInboxItems().size());
        
        profile.getInboxItems().remove(msg1);
        assertEquals(1, profile.getInboxItems().size());
    }

    @Test
    void testEdgeCaseIntegers() {
        profile.setAge(Integer.MIN_VALUE);
        profile.setMovementMinutes(Integer.MAX_VALUE);
        
        assertEquals(Integer.MIN_VALUE, profile.getAge());
        assertEquals(Integer.MAX_VALUE, profile.getMovementMinutes());
    }

    @Test
    void testLongStrings() {
        String longString = "A".repeat(500);
        
        profile.setName(longString);
        profile.setNationality(longString);
        profile.setEmail(longString + "@domain.com");
        
        assertEquals(longString, profile.getName());
        assertTrue(profile.getEmail().contains(longString));
    }

    @Test
    void testNullHandling() {
        Profile empty = new Profile();
        
        empty.setName(null);
        empty.setGender(null);
        empty.setPasswordHash(null);
        empty.setInboxItems(null);
        
        assertNull(empty.getName());
        assertNull(empty.getGender());
        assertNull(empty.getInboxItems());
    }
    
    @Test
    void testRegisteredAtCustomization() {
        LocalDateTime customTime = LocalDateTime.now().minusDays(5);
        profile.setRegisteredAt(customTime);
        
        assertEquals(customTime.truncatedTo(ChronoUnit.SECONDS), 
                     profile.getRegisteredAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void testProfileExerciseRelationship() {
        ProfileExercises exercise = new ProfileExercises();
        profile.getProfileExercises().add(exercise);
        
        assertTrue(profile.getProfileExercises().contains(exercise));
    }
    
    @Test
    void testCreatedMessagesRelationship() {
        CommunicationMessage msg = new CommunicationMessage();
        profile.getCreatedMessages().add(msg);
        
        assertEquals(1, profile.getCreatedMessages().size());
    }
}