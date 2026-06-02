package be.ucll.unit.model.dto;

import be.ucll.model.dto.CommunicationRecipientDTO;
import be.ucll.model.enums.ProfileRole;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunicationRecipientDTOTest {

    @Test
    void testDefaultConstructor() {
        CommunicationRecipientDTO dto = new CommunicationRecipientDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getRole());
    }

    @Test
    void testParameterizedConstructor() {
        CommunicationRecipientDTO dto = new CommunicationRecipientDTO(1L, "John Doe", ProfileRole.ADMIN);

        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getName());
        assertEquals(ProfileRole.ADMIN, dto.getRole());
    }

    @Test
    void testNullValues() {
        CommunicationRecipientDTO dto = new CommunicationRecipientDTO(null, null, null);

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getRole());
    }

    @Test
    void testLongName() {
        String longName = "A".repeat(255);
        CommunicationRecipientDTO dto = new CommunicationRecipientDTO(3L, longName, ProfileRole.END_USER);

        assertEquals(longName, dto.getName());
    }

    @Test
    void testBoundaryValues() {
        CommunicationRecipientDTO dto = new CommunicationRecipientDTO(Long.MAX_VALUE, "Test", null);

        assertEquals(Long.MAX_VALUE, dto.getId());
        assertNull(dto.getRole());
    }
}