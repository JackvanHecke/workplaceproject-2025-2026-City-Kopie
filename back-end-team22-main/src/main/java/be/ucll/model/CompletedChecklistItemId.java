package be.ucll.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class CompletedChecklistItemId implements Serializable {

    private Long benchId; // Changed from deviceId
    private Long checklistId;

    public CompletedChecklistItemId() {}

    public CompletedChecklistItemId(Long benchId, Long checklistId) {
        this.benchId = benchId;
        this.checklistId = checklistId;
    }

    public Long getBenchId() { return benchId; }
    public void setBenchId(Long benchId) { this.benchId = benchId; }

    public Long getChecklistId() { return checklistId; }
    public void setChecklistId(Long checklistId) { this.checklistId = checklistId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletedChecklistItemId that = (CompletedChecklistItemId) o;
        return Objects.equals(benchId, that.benchId) &&
               Objects.equals(checklistId, that.checklistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(benchId, checklistId);
    }
}