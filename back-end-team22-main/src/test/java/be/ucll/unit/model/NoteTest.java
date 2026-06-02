package be.ucll.unit.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import be.ucll.model.Location;
import be.ucll.model.Note;

class NoteTest {

    private Note note;
    private Location location;

    @BeforeEach
    void setUp() {
        location = mock(Location.class);
        // Note: The parameterized constructor automatically sets created/updatedAt to now()
        note = new Note(location, "Initial note content");
    }

    @Test
    void testDefaultConstructor() {
        Note emptyNote = new Note();

        assertNull(emptyNote.getId());
        assertNull(emptyNote.getLocation());
        assertNull(emptyNote.getContent());
        assertNull(emptyNote.getCreatedAt());
        assertNull(emptyNote.getUpdatedAt());
    }

    @Test
    void testSetters() {
        // Test id
        note.setId(10L);
        assertEquals(10L, note.getId());

        // Test location relationship
        Location newLocation = mock(Location.class);
        note.setLocation(newLocation);
        assertEquals(newLocation, note.getLocation());

        // Test manual timestamp setters
        LocalDateTime specificTime = LocalDateTime.of(2024, 1, 1, 12, 0);
        note.setCreatedAt(specificTime);
        assertEquals(specificTime, note.getCreatedAt());
        
        note.setUpdatedAt(specificTime);
        assertEquals(specificTime, note.getUpdatedAt());
    }

    @Test
    void testSetContentUpdatesTimestamp() throws InterruptedException {
        // Capture the original time set in setUp()
        LocalDateTime originalUpdateTime = note.getUpdatedAt();

        // Pause briefly to ensure System time advances (timestamps might have nanosecond precision)
        Thread.sleep(10);

        // Setting content should trigger: this.updatedAt = LocalDateTime.now();
        note.setContent("Updated content");

        assertEquals("Updated content", note.getContent());
        assertNotNull(note.getUpdatedAt());
        assertNotEquals(originalUpdateTime, note.getUpdatedAt());
        assertTrue(note.getUpdatedAt().isAfter(originalUpdateTime));
    }

    @Test
    void testNullHandling() {
        note.setId(null);
        note.setLocation(null);
        
        // Note: setContent(null) will actually set content to null 
        // BUT it will still update the updatedAt timestamp to now()
        note.setContent(null);
        
        // We manually set timestamps to null to test the getters/setters for nullability
        note.setCreatedAt(null);
        note.setUpdatedAt(null);

        assertNull(note.getId());
        assertNull(note.getLocation());
        assertNull(note.getContent());
        assertNull(note.getCreatedAt());
        assertNull(note.getUpdatedAt());
    }

    @Test
    void testEdgeCaseStrings() {
        String longString = "Text".repeat(1000); // Very long content

        note.setContent(longString);

        assertEquals(longString, note.getContent());
        assertNotNull(note.getUpdatedAt()); // Should still update timestamp
    }
}