package be.ucll.unit.model.dto;

import be.ucll.model.dto.CommunicationMessageDTO;
import be.ucll.model.dto.CommunicationRecipientDTO;
import be.ucll.model.enums.CommunicationCategory;
import be.ucll.model.enums.DeliveryChannel;
import be.ucll.model.enums.ProfileRole;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommunicationMessageDTOTest {

    private final LocalDateTime NOW = LocalDateTime.now();
    private final CommunicationRecipientDTO RECIPIENT = new CommunicationRecipientDTO(1L, "John Doe", null);

    @Test
    void testDefaultConstructor() {
        CommunicationMessageDTO dto = new CommunicationMessageDTO();
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getChannels());
        assertNull(dto.getExplicitRecipients());
    }

    @Test
    void testParameterizedConstructor() {
        CommunicationMessageDTO dto = new CommunicationMessageDTO(
            1L,
            "Title",
            "Content",
            CommunicationCategory.ACTIVITY,
            EnumSet.of(DeliveryChannel.APP_POPUP, DeliveryChannel.DEVICE_POPUP),
            NOW,
            NOW.plusDays(7),
            true,
            18,
            65,
            EnumSet.of(ProfileRole.END_USER),
            10L,
            "Alice Admin",
            NOW.minusDays(1),
            List.of(RECIPIENT)
        );
        assertEquals(1L, dto.getId());
        assertEquals("Title", dto.getTitle());
        assertEquals(2, dto.getChannels().size());
        assertEquals(ProfileRole.END_USER, dto.getTargetRoles().iterator().next());
        assertEquals(1, dto.getExplicitRecipients().size());
        assertEquals("Alice Admin", dto.getCreatedByName());
    }

    @Test
    void testSettersAndGetters() {
        CommunicationMessageDTO dto = new CommunicationMessageDTO();
        // Verify
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getChannels());
        assertNull(dto.getExplicitRecipients());
    }

    @Test
    void testNullHandling() {
        CommunicationMessageDTO dto = new CommunicationMessageDTO(
            null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null
        );

        assertNull(dto.getCategory());
        assertNull(dto.getStartsAt());
        assertNull(dto.getActive());
        assertNull(dto.getExplicitRecipients());
    }

    @Test
    void testBoundaryValues() {
        CommunicationMessageDTO dto = new CommunicationMessageDTO(
            Long.MAX_VALUE,
            "A".repeat(500),
            "B".repeat(10000),
            CommunicationCategory.OTHER,
            EnumSet.of(DeliveryChannel.values()[0]),
            LocalDateTime.MIN,
            LocalDateTime.MAX,
            false,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            null,
            99999L,
            "X".repeat(255),
            NOW,
            null
        );

        assertEquals(Long.MAX_VALUE, dto.getId());
        assertEquals(Integer.MAX_VALUE, dto.getMaxAge());
        assertEquals("OTHER", dto.getCategory().name()); // Verify enum name
    }

    @Test
    void testCollectionEdgeCases() {
        CommunicationMessageDTO dto = new CommunicationMessageDTO();

        assertNull(dto.getChannels());
        assertNull(dto.getTargetRoles());
        assertNull(dto.getExplicitRecipients());
    }

    // Helper method to generate test data
    private CommunicationMessageDTO createSampleDTO() {
        return new CommunicationMessageDTO(
            1L,
            "Test",
            "Content",
            CommunicationCategory.COURSE_SERIES,
            EnumSet.of(DeliveryChannel.APP_POPUP),
            NOW,
            null,
            true,
            null,
            null,
            null,
            100L,
            "Creator Name",
            NOW.minusDays(1),
            List.of(RECIPIENT)
        );
    }

    @Test
    void testOptionalFields() {
        CommunicationMessageDTO dto = createSampleDTO();
        assertNull(dto.getEndsAt()); // Nullable
        assertNull(dto.getMinAge());
        assertNull(dto.getMaxAge());
    }

    @Test
    void testGetCreatedByProfileID() {
        CommunicationMessageDTO dto = createSampleDTO();
        assertEquals(100L, dto.getCreatedByProfileId());
    }

    @Test
    void testGetCreatedAt() {
        CommunicationMessageDTO dto = createSampleDTO();
        assertEquals(NOW.minusDays(1), dto.getCreatedAt());
    }
}