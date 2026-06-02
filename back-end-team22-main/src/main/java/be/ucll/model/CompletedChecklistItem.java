package be.ucll.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "device_checklist_completion")
public class CompletedChecklistItem {

    @EmbeddedId
    private CompletedChecklistItemId id;

    @ManyToOne
    @MapsId("benchId") // Maps to 'benchId' in CompletedChecklistItemId
    @JoinColumn(name = "bench_id")
    @JsonBackReference("location-completion")
    private Location location;

    @ManyToOne
    @MapsId("checklistId") // Maps the 'checklistId' field in the EmbeddedId
    @JoinColumn(name = "checklist_id")
    @JsonBackReference("checklist-completion")
    private Checklist checklist;

    private LocalDateTime completedAt = LocalDateTime.now();

    public CompletedChecklistItem() {
        this.id = new CompletedChecklistItemId();
    }

    public CompletedChecklistItem(Location location, Checklist checklist) {
        this.location = location;
        this.checklist = checklist;
        this.id = new CompletedChecklistItemId(location.getBenchId(), checklist.getId());
    }

    public CompletedChecklistItemId getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

}