package be.ucll.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.model.*;
import be.ucll.model.enums.DeliveryChannel;
import be.ucll.model.enums.ProfileRole;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static be.ucll.model.enums.CommunicationCategory.*;
import static be.ucll.model.enums.DeliveryChannel.*;
import static be.ucll.model.enums.ProfileRole.*;

class CommunicationMessageTest {

    private CommunicationMessage message;
    private Profile creator;
    private Profile recipient;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        creator = new Profile();
        recipient = new Profile();
        message = new CommunicationMessage();

        message.setTitle("Important Update");
        message.setBody("System maintenance scheduled");
        message.setCategory(ACTIVITY);
        message.setStartsAt(now);
        message.setCreatedBy(creator);
        message.setCreatedAt(now);
    }

    @Test
    void testDefaultValues() {
        CommunicationMessage defaultMessage = new CommunicationMessage();

        assertEquals(ACTIVITY, message.getCategory());
        assertTrue(defaultMessage.getActive());
        assertNotNull(defaultMessage.getChannels());
        assertTrue(defaultMessage.getChannels().isEmpty());
    }

    @Test
    void testFieldAssignments() {
        assertEquals("Important Update", message.getTitle());
        assertEquals("System maintenance scheduled", message.getBody());
        assertEquals(creator, message.getCreatedBy());
        assertEquals(now, message.getCreatedAt());
        assertEquals(ACTIVITY, message.getCategory());
    }

    @Test
    void testChannelManagement() {
        Set<DeliveryChannel> channels = new HashSet<>();
        channels.add(MOBILE_POPUP);
        message.setChannels(channels);

        assertEquals(1, message.getChannels().size());
        assertTrue(message.getChannels().contains(MOBILE_POPUP));
        message.getChannels().remove(MOBILE_POPUP);
        assertEquals(0, message.getChannels().size());
    }

    @Test
    void testDateRanges() {
        LocalDateTime end = now.plusDays(7);
        message.setEndsAt(end);
        assertFalse(isActiveAt(message, now.minusDays(1)));
        assertTrue(isActiveAt(message, end));
        assertTrue(isActiveAt(message, now.plusDays(3)));
    }

    @Test
    void testOpenEndedRange() {
        message.setEndsAt(null);
        assertTrue(isActiveAt(message, now.plusYears(1)));
    }

    private boolean isActiveAt(CommunicationMessage message, LocalDateTime when) {
        return !when.isBefore(message.getStartsAt()) &&
               (message.getEndsAt() == null || !when.isAfter(message.getEndsAt()));
    }

    @Test
    void testInactivity() {
        message.setActive(false);
        assertFalse(message.getActive());
        // Even within date range
        assertTrue(isActiveAt(message, now));
    }

    @Test
    void testAgeFiltering() {
        message.setMinAge(18);
        message.setMaxAge(65);

        assertTrue(appliesToAge(message,30));
        assertFalse(appliesToAge(message, 17));
        assertFalse(appliesToAge(message, 66));

        // Edge cases
        assertTrue(appliesToAge(message, 18));
        assertTrue(appliesToAge(message, 65));
    }

    @Test
    void testRoleFiltering() {
        message.getTargetRoles().add(END_USER);
        message.getTargetRoles().add(COACH);

        assertTrue(appliesToRole(message, END_USER));
        assertTrue(appliesToRole(message, COACH));
        assertFalse(appliesToRole(message, ADMIN));
        assertFalse(appliesToRole(message, null));
    }

    @Test
    void testExplicitRecipients() {
        message.getExplicitRecipients().add(recipient);
        assertEquals(1, message.getExplicitRecipients().size());
        assertTrue(message.getExplicitRecipients().contains(recipient));

        message.getExplicitRecipients().remove(recipient);
        assertTrue(message.getExplicitRecipients().isEmpty());
    }

    @Test
    void testOptionalCollections() {
        CommunicationMessage nullCollections = new CommunicationMessage();
        nullCollections.setChannels(null);
        nullCollections.setTargetRoles(null);
        nullCollections.setExplicitRecipients(null);

        assertNull(nullCollections.getChannels());
        assertNull(nullCollections.getTargetRoles());
        assertNull(nullCollections.getExplicitRecipients());
    }

    @Test
    void testNegativeAgeValues() {
        message.setMinAge(-5);
        message.setMaxAge(-1);

        assertFalse(appliesToAge(message, 30)); // Always false with negative ages
        assertFalse(appliesToAge(message, 0));
    }

    @Test
    void testMinMaxOverride() {
        message.setMaxAge(30);
        message.setMinAge(40); // Inverted

        assertFalse(appliesToAge(message, 35));
    }

    @Test
    void testEmptyRoleTargetingHandling() {
        assertFalse(appliesToRole(message, END_USER)); // No roles set
        message.setTargetRoles(new HashSet<>());
        assertFalse(appliesToRole(message, END_USER));
    }

    @Test
    void testLongStrings() {
        String longTitle = "A".repeat(500);
        String longBody = "B".repeat(10000);

        message.setTitle(longTitle);
        message.setBody(longBody);

        assertEquals(longTitle, message.getTitle());
        assertEquals(longBody, message.getBody());
    }

    @Test
    void testNullHandling() {
        message.setTitle(null);
        message.setBody(null);
        message.setActive(null);
        message.setCreatedBy(null);

        assertNull(message.getTitle());
        assertNull(message.getBody());
        assertNull(message.getActive());
        assertNull(message.getCreatedBy());
    }

    private boolean appliesToAge(CommunicationMessage message, int age) {
        return message.getMinAge() <= age && (message.getMaxAge() == null || message.getMaxAge() >= age);
    }

    private boolean appliesToRole(CommunicationMessage message, ProfileRole role) {
        return message.getTargetRoles().contains(role);
    }
}