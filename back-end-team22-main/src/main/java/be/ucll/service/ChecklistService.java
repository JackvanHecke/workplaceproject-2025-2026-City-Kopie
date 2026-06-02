package be.ucll.service;

// ... imports for List, Autowired, Service ...
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.model.Checklist;
import be.ucll.model.CompletedChecklistItem;
import be.ucll.model.Location;
import be.ucll.repository.ChecklistRepository;
import be.ucll.repository.CompletedChecklistItemRepository;
import be.ucll.repository.LocationRepository; 
import be.ucll.repository.ProfileRepository; 

@Service
public class ChecklistService { 

    private ChecklistRepository checklistRepository; 
    private CompletedChecklistItemRepository completedChecklistItemRepository;
    private LocationRepository locationRepository;

    @Autowired
    public ChecklistService(
        ChecklistRepository checklistRepository, 
        ProfileRepository profileRepository,
        CompletedChecklistItemRepository completedChecklistItemRepository,
        LocationRepository locationRepository) 
    {
        this.checklistRepository = checklistRepository;
        this.completedChecklistItemRepository = completedChecklistItemRepository;
        this.locationRepository = locationRepository;}

    // ... (getAllChecklists and addChecklist methods remain the same) ...

    public List<Checklist> getAllChecklists() {
        return checklistRepository.findAll();
    }

    /**
     * Adds a new Checklist item after checking if one with the same name already exists.
     * @param checklistData The Checklist entity to save.
     * @return The saved Checklist entity.
     * @throws RuntimeException if a Checklist with the same name already exists.
     */
    public Checklist addChecklist(Checklist checklistData) { 
        Checklist existingChecklist = checklistRepository.findByName(checklistData.getName());
        if (existingChecklist != null) {
            throw new RuntimeException("Checklist already exists.");
        }
        checklistRepository.save(checklistData);
        return checklistData;
    }

    public void toggleChecklistCompletion(Long locationId, Long checklistId) { 
        
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
                
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));

        // 1. Check if the completion record already exists for this (Profile, Checklist) pair
        CompletedChecklistItem existingCompletion = completedChecklistItemRepository
            .findByLocationAndChecklist(location, checklist)
            .orElse(null); // Assuming you create this repository method

        if (existingCompletion != null) {
            // 2. If it exists, DELETE it (Uncheck/Remove completion)
            completedChecklistItemRepository.delete(existingCompletion);
            
            // Optional cleanup on the entity side (Good practice)
            location.getCompletedChecklistItems().remove(existingCompletion);
            checklist.getCompletionRecords().remove(existingCompletion);
            
        } else { 
            // 3. If it doesn't exist, CREATE a new record (Check/Mark completed)
            CompletedChecklistItem newCompletion = new CompletedChecklistItem(location, checklist);
            completedChecklistItemRepository.save(newCompletion);
            
            // Link new record back to the entities
            location.getCompletedChecklistItems().add(newCompletion);
            checklist.getCompletionRecords().add(newCompletion);
        }
        
        // Save the profile/checklist to update the collections (if needed, JPA often manages this)
        // Explicitly saving the entities involved is robust.
        locationRepository.save(location);
        checklistRepository.save(checklist);
    }
}