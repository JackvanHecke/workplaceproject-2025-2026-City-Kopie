package be.ucll.unit.service;

import be.ucll.model.*;
import be.ucll.repository.*;
import be.ucll.service.ChecklistService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChecklistServiceTest {

    @Mock
    private ChecklistRepository checklistRepository;

    @Mock
    private ProfileRepository profileRepository; // Not used but required for constructor

    @Mock
    private CompletedChecklistItemRepository completedChecklistItemRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private ChecklistService checklistService;

    private Location location;
    private Checklist checklist;
    private final Long locationId = 1L;
    private final Long checklistId = 10L;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setBenchId(locationId);
        checklist = new Checklist();
        checklist.setId(checklistId);
    }

    @Test
    void getAllChecklists_ReturnsChecklists() {
        // Arrange
        when(checklistRepository.findAll()).thenReturn(List.of(checklist));
        
        // Act
        List<Checklist> result = checklistService.getAllChecklists();
        
        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(checklist));
    }

    @Test
    void addChecklist_NewChecklist_SavesSuccessfully() {
        // Arrange
        when(checklistRepository.findByName("New Checklist")).thenReturn(null);
        when(checklistRepository.save(any())).then(returnsFirstArg());
        
        // Act
        checklist.setName("New Checklist");
        Checklist result = checklistService.addChecklist(checklist);
        
        // Assert
        assertEquals("New Checklist", result.getName());
        verify(checklistRepository).save(checklist);
    }

    @Test
    void addChecklist_DuplicateName_ThrowsException() {
        // Arrange
        when(checklistRepository.findByName("Existing")).thenReturn(new Checklist());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> checklistService.addChecklist(new Checklist("Existing", "Info", null)));
        
        assertEquals("Checklist already exists.", exception.getMessage());
        verify(checklistRepository, never()).save(any());
    }

    @Test
    void toggleChecklistCompletion_NotCompleted_CreatesCompletion() {
        // Arrange
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        when(checklistRepository.findById(checklistId)).thenReturn(Optional.of(checklist));
        when(completedChecklistItemRepository.findByLocationAndChecklist(location, checklist))
            .thenReturn(Optional.empty());
        when(completedChecklistItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(locationRepository.save(location)).thenReturn(location);
        when(checklistRepository.save(checklist)).thenReturn(checklist);
        
        // Act
        checklistService.toggleChecklistCompletion(locationId, checklistId);
        
        // Assert
        verify(completedChecklistItemRepository).save(any(CompletedChecklistItem.class));
        assertEquals(1, location.getCompletedChecklistItems().size());
        assertEquals(1, checklist.getCompletionRecords().size());
        verify(locationRepository).save(location);
        verify(checklistRepository).save(checklist);
    }

    @Test
    void toggleChecklistCompletion_AlreadyCompleted_DeletesCompletion() {
        // Arrange
        CompletedChecklistItem existing = new CompletedChecklistItem(location, checklist);
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        when(checklistRepository.findById(checklistId)).thenReturn(Optional.of(checklist));
        when(completedChecklistItemRepository.findByLocationAndChecklist(location, checklist))
            .thenReturn(Optional.of(existing));
        when(locationRepository.save(location)).thenReturn(location);
        when(checklistRepository.save(checklist)).thenReturn(checklist);
        
        // Prepopulate collections
        location.getCompletedChecklistItems().add(existing);
        checklist.getCompletionRecords().add(existing);
        
        // Act
        checklistService.toggleChecklistCompletion(locationId, checklistId);
        
        // Assert
        verify(completedChecklistItemRepository).delete(existing);
        assertTrue(location.getCompletedChecklistItems().isEmpty());
        assertTrue(checklist.getCompletionRecords().isEmpty());
        verify(locationRepository).save(location);
        verify(checklistRepository).save(checklist);
    }

    @Test
    void toggleChecklistCompletion_MissingLocation_ThrowsException() {
        // Arrange
        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> checklistService.toggleChecklistCompletion(locationId, checklistId));
        
        assertEquals("Location not found", exception.getMessage());
        verify(checklistRepository, never()).findById(any());
    }

    @Test
    void toggleChecklistCompletion_MissingChecklist_ThrowsException() {
        // Arrange
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        when(checklistRepository.findById(checklistId)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> checklistService.toggleChecklistCompletion(locationId, checklistId));
        
        assertEquals("Checklist not found", exception.getMessage());
    }

    @Test
    void toggleChecklistCompletion_NullId_ThrowsException() {
        // Act & Assert - Should fail at repository level, but handles null gracefully
        assertThrows(RuntimeException.class,
            () -> checklistService.toggleChecklistCompletion(null, null));
    }
}