package be.ucll.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.model.Location;
import be.ucll.model.Note;
import be.ucll.repository.LocationRepository;
import be.ucll.repository.NoteRepository;

@Service
public class NoteService {

    private NoteRepository noteRepository;
    private LocationRepository locationRepository;
    
    @Autowired
    private NoteService(NoteRepository noteRepository, LocationRepository locationRepository) {
        this.noteRepository = noteRepository;
        this.locationRepository = locationRepository;
    }


    public List<Note> getAllNotesForLocation(Long locationId) {
        Location location = locationRepository.findById(locationId)
            .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + locationId));
        return noteRepository.findByLocationOrderByUpdatedAtDesc(location);
    }

    public Note createNote(Long locationId, String content) {
        Location location = locationRepository.findById(locationId)
            .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + locationId));
        
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Note content cannot be empty");
        }

        Note note = new Note(location, content);
        return noteRepository.save(note);
    }

    public Note updateNote(Long noteId, String content) {
        Note note = noteRepository.findById(noteId)
            .orElseThrow(() -> new IllegalArgumentException("Note not found with id: " + noteId));
        
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Note content cannot be empty");
        }

        note.setContent(content);
        return noteRepository.save(note);
    }

    public void deleteNote(Long noteId) {
        if (!noteRepository.existsById(noteId)) {
            throw new IllegalArgumentException("Note not found with id: " + noteId);
        }
        noteRepository.deleteById(noteId);
    }

    public Note getNoteById(Long noteId) {
        return noteRepository.findById(noteId)
            .orElseThrow(() -> new IllegalArgumentException("Note not found with id: " + noteId));
    }
}
