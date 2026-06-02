package be.ucll.model.dto;

import java.time.LocalDateTime;

public class CoachAvailabilityUpsertRequest {
    private Long benchId; // optional
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private Boolean isRecurring;
    private String recurrenceRule;
    private String note;

    public Long getBenchId() { return benchId; }
    public void setBenchId(Long benchId) { this.benchId = benchId; }

    public LocalDateTime getAvailableFrom() { return availableFrom; }
    public void setAvailableFrom(LocalDateTime availableFrom) { this.availableFrom = availableFrom; }

    public LocalDateTime getAvailableTo() { return availableTo; }
    public void setAvailableTo(LocalDateTime availableTo) { this.availableTo = availableTo; }

    public Boolean getIsRecurring() { return isRecurring; }
    public void setIsRecurring(Boolean isRecurring) { this.isRecurring = isRecurring; }

    public String getRecurrenceRule() { return recurrenceRule; }
    public void setRecurrenceRule(String recurrenceRule) { this.recurrenceRule = recurrenceRule; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
