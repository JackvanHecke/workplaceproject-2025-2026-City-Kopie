package be.ucll.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.model.Checklist;
import be.ucll.model.CompletedChecklistItem;
import be.ucll.model.CompletedChecklistItemId;
import be.ucll.model.Location;

public interface CompletedChecklistItemRepository extends JpaRepository<CompletedChecklistItem, CompletedChecklistItemId> {

    Optional<CompletedChecklistItem> findByLocationAndChecklist(Location location, Checklist checklist);

}