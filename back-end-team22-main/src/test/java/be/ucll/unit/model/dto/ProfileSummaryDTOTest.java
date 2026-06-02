package be.ucll.unit.model.dto;

import org.junit.jupiter.api.Test;

import be.ucll.model.dto.ProfileSummaryDTO;

import static org.junit.jupiter.api.Assertions.*;

class ProfileSummaryDTOTest {

    @Test
    void testDefaultConstructor() {
        ProfileSummaryDTO dto = new ProfileSummaryDTO();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getEmail());
    }

    @Test
    void testParameterizedConstructor() {
        ProfileSummaryDTO dto = new ProfileSummaryDTO(1L, "John Doe", "john@example.com");

        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getName());
        assertEquals("john@example.com", dto.getEmail());
    }

    @Test
    void testSetters() {
        ProfileSummaryDTO dto = new ProfileSummaryDTO();
        dto.setId(2L);
        dto.setName("Jane Smith");
        dto.setEmail("jane@example.org");

        assertEquals(2L, dto.getId());
        assertEquals("Jane Smith", dto.getName());
        assertEquals("jane@example.org", dto.getEmail());
    }

    @Test
    void testNullValues() {
        ProfileSummaryDTO dto = new ProfileSummaryDTO(null, null, null);

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getEmail());
    }

    @Test
    void testLongStrings() {
        String longName = "A".repeat(500);
        String longEmail = "B".repeat(100) + "@domain.com";

        ProfileSummaryDTO dto = new ProfileSummaryDTO();
        dto.setName(longName);
        dto.setEmail(longEmail);

        assertEquals(longName, dto.getName());
        assertEquals(longEmail, dto.getEmail());
    }

    @Test
    void testEquality() {
        ProfileSummaryDTO dto1 = new ProfileSummaryDTO(1L, "Same", "same@test.com");
        ProfileSummaryDTO dto2 = new ProfileSummaryDTO(1L, "Same", "same@test.com");

        assertEquals(dto1.getId(), dto2.getId());
        assertEquals(dto1.getName(), dto2.getName());
        assertEquals(dto1.getEmail(), dto2.getEmail());
    }

    @Test
    void testBoundaryIds() {
        ProfileSummaryDTO minDTO = new ProfileSummaryDTO(Long.MIN_VALUE, "Min", null);
        ProfileSummaryDTO maxDTO = new ProfileSummaryDTO(Long.MAX_VALUE, "Max", null);

        assertEquals(Long.MIN_VALUE, minDTO.getId());
        assertEquals(Long.MAX_VALUE, maxDTO.getId());
    }
}