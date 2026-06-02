package be.ucll.unit.model.dto;

import be.ucll.model.dto.ProfileMessageDTO;
import be.ucll.model.enums.DeliveryChannel;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.EnumSet;
import static org.junit.jupiter.api.Assertions.*;

class ProfileMessageDTOTest {

    private final LocalDateTime NOW = LocalDateTime.now();

    @Test
    void testDefaultConstructor() {
        ProfileMessageDTO dto = new ProfileMessageDTO();

        assertNull(dto.getId());
        assertNull(dto.getMessageId());
        assertNull(dto.getRead());
        assertNull(dto.getChannels());
    }

    @Test
    void testParameterizedConstructor() {
        ProfileMessageDTO dto = new ProfileMessageDTO(
            1L,
            100L,
            "Important Update",
            "System maintenance scheduled...",
            false,
            NOW,
            EnumSet.of(DeliveryChannel.PROFILE_MESSAGE)
        );

        assertEquals(1L, dto.getId());
        assertEquals(100L, dto.getMessageId());
        assertEquals("Important Update", dto.getTitle());
        assertFalse(dto.getRead());
        assertEquals(NOW, dto.getDeliveredAt());
        assertEquals(1, dto.getChannels().size());
    }

    @Test
    void testSettersAndGetters() {
        ProfileMessageDTO dto = new ProfileMessageDTO();

        dto.setId(2L);
        dto.setMessageId(200L);
        dto.setTitle("Urgent");
        dto.setPreview("New features available");
        dto.setRead(true);
        dto.setDeliveredAt(NOW.plusDays(1));
        dto.setChannels(EnumSet.of(DeliveryChannel.APP_POPUP, DeliveryChannel.DEVICE_POPUP));

        assertEquals(2L, dto.getId());
        assertEquals("Urgent", dto.getTitle());
        assertTrue(dto.getRead());
        assertEquals(NOW.plusDays(1), dto.getDeliveredAt());
        assertEquals(2, dto.getChannels().size());
    }

    @Test
    void testNullValues() {
        ProfileMessageDTO dto = new ProfileMessageDTO(
            null, null, null, null, null, null, null
        );

        assertNull(dto.getId());
        assertNull(dto.getMessageId());
        assertNull(dto.getTitle());
        assertNull(dto.getPreview());
        assertNull(dto.getRead());
        assertNull(dto.getDeliveredAt());
        assertNull(dto.getChannels());
    }

    @Test
    void testLongStrings() {
        String longTitle = "A".repeat(500);
        String longPreview = "B".repeat(1000);

        ProfileMessageDTO dto = new ProfileMessageDTO();
        dto.setTitle(longTitle);
        dto.setPreview(longPreview);

        assertEquals(longTitle, dto.getTitle());
        assertEquals(longPreview, dto.getPreview());
    }

    @Test
    void testChannelsCollection() {
        ProfileMessageDTO dto = new ProfileMessageDTO();

        // Set empty collection
        dto.setChannels(EnumSet.noneOf(DeliveryChannel.class));
        assertTrue(dto.getChannels().isEmpty());

        // Set null
        dto.setChannels(null);
        assertNull(dto.getChannels());
    }

    @Test
    void testDateBoundaries() {
        ProfileMessageDTO dto = new ProfileMessageDTO();

        dto.setDeliveredAt(LocalDateTime.MIN);
        assertEquals(LocalDateTime.MIN, dto.getDeliveredAt());

        dto.setDeliveredAt(LocalDateTime.MAX);
        assertEquals(LocalDateTime.MAX, dto.getDeliveredAt());
    }

    @Test
    void testBooleanEdgeCases() {
        ProfileMessageDTO dto = new ProfileMessageDTO();

        dto.setRead(true);
        assertTrue(dto.getRead());

        dto.setRead(false);
        assertFalse(dto.getRead());

        dto.setRead(null);
        assertNull(dto.getRead());
    }
}