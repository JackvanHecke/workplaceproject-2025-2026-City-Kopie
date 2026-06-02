package be.ucll.unit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import be.ucll.controller.NoteRestController;
import be.ucll.model.Note;
import be.ucll.service.NoteService;

@ExtendWith(MockitoExtension.class)
class NoteRestControllerTest {

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteRestController controller;

    @Mock
    private WebRequest webRequest;

    private Note note;

    @BeforeEach
    void setUp() {
        note = new Note();
        note.setId(1L);
        // Assuming Note has a setContent method based on the controller usage
        note.setContent("Test Note Content"); 
        // Assuming Note has a locationId or relationship, but service handles the logic
    }

    @Test
    void getAllNotesForLocation_ExistingId_ReturnsListOfNotes() {
        // Arrange
        Long locationId = 1L;
        when(noteService.getAllNotesForLocation(locationId)).thenReturn(List.of(note));

        // Act
        List<Note> response = controller.getAllNotesForLocation(locationId);

        // Assert
        assertEquals(1, response.size());
        assertEquals("Test Note Content", response.get(0).getContent());
        verify(noteService, times(1)).getAllNotesForLocation(locationId);
    }

    @Test
    void createNote_ValidInput_ReturnsCreatedNote() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("locationId", 1L);
        request.put("content", "New Note Content");

        // Mocking the return object to match expected logic
        Note createdNote = new Note();
        createdNote.setContent("New Note Content");
        
        when(noteService.createNote(1L, "New Note Content")).thenReturn(createdNote);

        // Act
        Note response = controller.createNote(request);

        // Assert
        assertNotNull(response);
        assertEquals("New Note Content", response.getContent());
        verify(noteService, times(1)).createNote(1L, "New Note Content");
    }

    @Test
    void updateNote_ValidIdAndInput_ReturnsUpdatedNote() {
        // Arrange
        Long noteId = 1L;
        Map<String, String> request = new HashMap<>();
        request.put("content", "Updated Content");

        Note updatedNote = new Note();
        updatedNote.setId(noteId);
        updatedNote.setContent("Updated Content");

        when(noteService.updateNote(noteId, "Updated Content")).thenReturn(updatedNote);

        // Act
        Note response = controller.updateNote(noteId, request);

        // Assert
        assertEquals(noteId, response.getId());
        assertEquals("Updated Content", response.getContent());
        verify(noteService, times(1)).updateNote(noteId, "Updated Content");
    }

    @Test
    void deleteNote_ExistingId_ReturnsSuccessMessage() {
        // Arrange
        Long noteId = 1L;
        doNothing().when(noteService).deleteNote(noteId);

        // Act
        Map<String, String> response = controller.deleteNote(noteId);

        // Assert
        assertNotNull(response);
        assertEquals("Note deleted successfully", response.get("message"));
        verify(noteService, times(1)).deleteNote(noteId);
    }

    @Test
    void deleteNote_NonExistingId_ThrowsException() {
        // Arrange
        Long noteId = 99L;
        doThrow(new RuntimeException("Note not found"))
            .when(noteService).deleteNote(noteId);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> controller.deleteNote(noteId));

        assertEquals("Note not found", exception.getMessage());
    }

    @Test
    void handleDomainException_ReturnsBadRequestResponseEntity() {
        // Arrange
        String errorMessage = "Something went wrong";
        RuntimeException ex = new RuntimeException(errorMessage);

        // Act
        // We pass the mocked webRequest here because the method signature requires it
        ResponseEntity<Map<String, String>> response = controller.handleDomainException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().get("Error: "));
    }
}