package be.ucll.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "checklist") // Renamed table from "section"
@Entity
public class Checklist { // Renamed class from Section

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


    private String name; // Name of the Checklist item
    @Column(columnDefinition = "TEXT")
    private String info; // Information about the Checklist item

    // NEW: @ManyToOne relationship back to the parent Phase
    @ManyToOne
    @JoinColumn(name = "phase_id", referencedColumnName = "id")
    @JsonBackReference("phase-checklists")
    private Phase phase;

    // ADD the One-to-Many relationship to the new Join Entity
    @OneToMany(mappedBy = "checklist")
    @JsonManagedReference("checklist-completion")
    private Set<CompletedChecklistItem> completionRecords = new HashSet<>();

    public Checklist() {
    }

    public Checklist(String name, String info, Phase phase) {
        this.name = name;
        this.info = info;
        this.phase = phase;
    }

    // Getters and Setters...

    public Set<CompletedChecklistItem> getCompletionRecords() {
        return completionRecords;
    }

    public void setCompletionRecords(Set<CompletedChecklistItem> completionRecords) {
        this.completionRecords = completionRecords;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) { this.id = id; }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}