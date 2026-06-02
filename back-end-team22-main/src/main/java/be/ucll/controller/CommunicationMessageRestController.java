package be.ucll.controller;

import be.ucll.model.dto.CommunicationMessageCreateDTO;
import be.ucll.model.dto.CommunicationMessageDTO;
import be.ucll.service.CommunicationMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/communications")
@CrossOrigin(origins = "http://localhost:8000")
public class CommunicationMessageRestController {

    private final CommunicationMessageService messageService;

    public CommunicationMessageRestController(CommunicationMessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public List<CommunicationMessageDTO> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/{id}")
    public CommunicationMessageDTO getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id);
    }

    @PostMapping
    public ResponseEntity<CommunicationMessageDTO> createMessage(
            @RequestBody CommunicationMessageCreateDTO dto
    ) {
        CommunicationMessageDTO created = messageService.createMessage(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommunicationMessageDTO> updateMessage(
            @PathVariable Long id,
            @RequestBody CommunicationMessageCreateDTO dto
    ) {
        CommunicationMessageDTO updated = messageService.updateMessage(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    // For the app: get active messages for a specific profile
    @GetMapping("/profile/{profileId}")
    public List<CommunicationMessageDTO> getActiveForProfile(@PathVariable Long profileId) {
        return messageService.getActiveMessagesForProfile(profileId);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
