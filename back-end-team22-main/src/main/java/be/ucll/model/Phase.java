package be.ucll.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "phase") // Renamed table from "checklist"
@Entity
public class Phase { // Renamed class from Checklist

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Name of the Phase

    // NEW: One-to-Many relationship to the child Checklist items
@OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonManagedReference("phase-checklists")
private Set<Checklist> checklists = new HashSet<>();


    public Phase() {
    }

    public Phase(String name) {
        setName(name);
    }

    // Getters and Setters...

    // Add methods to manage the checklists collection
    public void addChecklist(Checklist checklist) {
        this.checklists.add(checklist);
        checklist.setPhase(this);
    }

    public void removeChecklist(Checklist checklist) {
        this.checklists.remove(checklist);
        checklist.setPhase(null);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Checklist> getChecklists() {
        return checklists;
    }

    public void setChecklists(Set<Checklist> checklists) {
        this.checklists = checklists;
    }
}