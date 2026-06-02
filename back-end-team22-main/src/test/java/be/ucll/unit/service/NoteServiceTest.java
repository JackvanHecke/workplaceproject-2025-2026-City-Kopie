package be.ucll.unit.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.model.Location;
import be.ucll.model.Note;
import be.ucll.repository.LocationRepository;
import be.ucll.repository.NoteRepository;
import be.ucll.service.NoteService;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private NoteService noteService;

    private Location location;
    private Note note;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setBenchId(1L);
        location.setBenchName("Central Park Bench");

        note = new Note();
        note.setId(1L);
        note.setContent("This is a test note.");
        note.setLocation(location);
    }

    @Test
    void getAllNotesForLocation_ExistingLocationId_ReturnsListOfNotes() {
        // Arrange
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(noteRepository.findByLocationOrderByUpdatedAtDesc(location)).thenReturn(Arrays.asList(note));

        // Act
        List<Note> result = noteService.getAllNotesForLocation(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals("This is a test note.", result.get(0).getContent());
        verify(locationRepository, times(1)).findById(1L);
        verify(noteRepository, times(1)).findByLocationOrderByUpdatedAtDesc(location);
    }

    @Test
    void getAllNotesForLocation_NonExistingLocationId_ThrowsException() {
        // Arrange
        when(locationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> noteService.getAllNotesForLocation(99L));

        assertEquals("Location not found with id: 99", exception.getMessage());
        verify(noteRepository, never()).findByLocationOrderByUpdatedAtDesc(any());
    }

    @Test
    void createNote_ValidInput_ReturnsSavedNote() {
        // Arrange
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> {
            Note savedNote = invocation.getArgument(0);
            savedNote.setId(1L); // Simulate DB ID generation
            return savedNote;
        });

        // Act
        Note result = noteService.createNote(1L, "New Note Content");

        // Assert
        assertNotNull(result);
        assertEquals("New Note Content", result.getContent());
        assertEquals(location, result.getLocation());
        verify(locationRepository, times(1)).findById(1L);
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void createNote_NonExistingLocation_ThrowsException() {
        // Arrange
        when(locationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> noteService.createNote(99L, "Content"));

        assertEquals("Location not found with id: 99", exception.getMessage());
        verify(noteRepository, never()).save(any());
    }

    @Test
    void createNote_EmptyContent_ThrowsException() {
        // Arrange
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> noteService.createNote(1L, ""));

        assertEquals("Note content cannot be empty", exception.getMessage());
        verify(noteRepository, never()).save(any());
    }

    @Test
    void updateNote_ExistingIdAndValidContent_ReturnsUpdatedNote() {
        // Arrange
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        // Act
        Note result = noteService.updateNote(1L, "Updated Content");

        // Assert
        assertEquals("Updated Content", result.getContent());
        verify(noteRepository, times(1)).findById(1L);
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void updateNote_NonExistingId_ThrowsException() {
        // Arrange
        when(noteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> noteService.updateNote(99L, "Content"));

        assertEquals("Note not found with id: 99", exception.getMessage());
        verify(noteRepository, never()).save(any());
    }

    @Test
    void updateNote_NullContent_ThrowsException() {
        // Arrange
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> noteService.updateNote(1L, null));

        assertEquals("Note content cannot be empty", exception.getMessage());
        verify(noteRepository, never()).save(any());
    }

    @Test
    void deleteNote_ExistingId_DeletesSuccessfully() {
        // Arrange
        when(noteRepository.existsById(1L)).thenReturn(true);

        // Act
        noteService.deleteNote(1L);

        // Assert
        verify(noteRepository, times(1)).existsById(1L);
        verify(noteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNote_NonExistingId_ThrowsException() {
        // Arrange
        when(noteRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> noteService.deleteNote(99L));

        assertEquals("Note not found with id: 99", exception.getMessage());
        verify(noteRepository, times(1)).existsById(99L);
        verify(noteRepository, never()).deleteById(anyLong());
    }

    @Test
    void getNoteById_ExistingId_ReturnsNote() {
        // Arrange
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        // Act
        Note result = noteService.getNoteById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(noteRepository, times(1)).findById(1L);
    }

    @Test
    void getNoteById_NonExistingId_ThrowsException() {
        // Arrange
        when(noteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> noteService.getNoteById(99L));

        assertEquals("Note not found with id: 99", exception.getMessage());
    }
}