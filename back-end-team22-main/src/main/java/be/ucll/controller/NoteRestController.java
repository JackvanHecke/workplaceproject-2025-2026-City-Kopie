package be.ucll.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import be.ucll.model.Note;
import be.ucll.service.NoteService;

@RestController
@RequestMapping("/notes")
@CrossOrigin(origins = "http://localhost:8000")
public class NoteRestController {

    private final NoteService noteService;

    public NoteRestController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<Note> getAllNotesForLocation(@RequestParam Long locationId) {
        return noteService.getAllNotesForLocation(locationId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Note createNote(@RequestBody Map<String, Object> request) {
        Long locationId = Long.parseLong(request.get("locationId").toString());
        String content = request.get("content").toString();

        return noteService.createNote(locationId, content);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String content = request.get("content");
        return noteService.updateNote(id, content);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Note deleted successfully");
        return successResponse;
    }

    // Centrale Exception Handler
    @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class, RuntimeException.class })
    public ResponseEntity<Map<String, String>> handleDomainException(RuntimeException ex, WebRequest request) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("Error: ", ex.getMessage());

        // Retourneer BAD_REQUEST voor consistentie
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}