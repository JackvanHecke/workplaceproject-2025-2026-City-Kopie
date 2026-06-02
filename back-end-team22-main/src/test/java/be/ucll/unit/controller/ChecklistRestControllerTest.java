package be.ucll.unit.controller;

import be.ucll.controller.ChecklistRestController;
import be.ucll.model.Checklist;
import be.ucll.service.ChecklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChecklistRestControllerTest {

    @Mock
    private ChecklistService checklistService;

    @InjectMocks
    private ChecklistRestController checklistRestController;

    private Checklist checklist;
    private final Long userId = 1L;
    private final Long checklistId = 10L;

    @BeforeEach
    void setUp() {
        checklist = new Checklist();
        checklist.setId(checklistId);
        checklist.setName("Safety Checklist");
    }

    @Test
    void getAllChecklists_ReturnsListOfChecklists() {
        // Arrange
        when(checklistService.getAllChecklists()).thenReturn(List.of(checklist));
        
        // Act
        List<Checklist> response = checklistRestController.getAllChecklists();
        
        // Assert
        assertEquals(1, response.size());
        assertEquals(checklist, response.get(0));
        verify(checklistService, times(1)).getAllChecklists();
    }

    @Test
    void getAllChecklists_EmptyList_ReturnsEmptyList() {
        // Arrange
        when(checklistService.getAllChecklists()).thenReturn(Collections.emptyList());
        
        // Act
        List<Checklist> response = checklistRestController.getAllChecklists();
        
        // Assert
        assertTrue(response.isEmpty());
    }

    @Test
    void addChecklist_ValidChecklist_ReturnsCreatedChecklist() {
        // Arrange
        when(checklistService.addChecklist(any(Checklist.class))).thenReturn(checklist);
        
        // Act
        Checklist response = checklistRestController.addChecklist(checklist);
        
        // Assert
        assertEquals(checklist, response);
        verify(checklistService, times(1)).addChecklist(checklist);
    }

    @Test
    void addChecklist_DuplicateChecklist_ThrowsException() {
        // Arrange
        String errorMessage = "Checklist already exists.";
        when(checklistService.addChecklist(any(Checklist.class)))
            .thenThrow(new RuntimeException(errorMessage));

        assertThrows(RuntimeException.class, () -> checklistRestController.addChecklist(checklist));
    }

    @Test
    void completeChecklist_ValidRequest_ReturnsOk() {
        // Arrange - No exception thrown means success
        doNothing().when(checklistService).toggleChecklistCompletion(userId, checklistId);
        
        // Act
        ResponseEntity<Void> response = 
            checklistRestController.completeOrRemoveChecklist(checklistId, userId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(checklistService, times(1)).toggleChecklistCompletion(userId, checklistId);
    }

    @Test
    void completeChecklist_ProfileNotFound_HandlesException() {
        // Arrange
        String errorMessage = "Profile not found";
        doThrow(new RuntimeException(errorMessage))
            .when(checklistService).toggleChecklistCompletion(userId, checklistId);
        assertThrows(RuntimeException.class, () -> checklistRestController.completeOrRemoveChecklist(checklistId, userId));
    }

    @Test
    void completeChecklist_ChecklistNotFound_HandlesException() {
        // Arrange
        String errorMessage = "Checklist not found";
        doThrow(new RuntimeException(errorMessage))
            .when(checklistService).toggleChecklistCompletion(userId, checklistId);
        assertThrows(RuntimeException.class, () -> checklistRestController.completeOrRemoveChecklist(checklistId, userId));
    }

    @Test
    void handleRuntimeException_ReturnsProperResponse() {
        // Arrange
        String errorMessage = "Test error message";
        RuntimeException ex = new RuntimeException(errorMessage);
        
        // Act
        ResponseEntity<Map<String, String>> response = 
            checklistRestController.handleDomainException(ex, null);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}