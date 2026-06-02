package be.ucll.model.dto;

import java.time.LocalDateTime;

public class CoachAvailabilityDTO {
    private Long id;
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private String note;
    private Boolean isRecurring;
    private String recurrenceRule;
    private LocationDTO bench; // optional bench info
    private LocalDateTime createdAt;

    public CoachAvailabilityDTO(Long id, LocalDateTime availableFrom, LocalDateTime availableTo,
                                String note, Boolean isRecurring, String recurrenceRule,
                                LocationDTO bench, LocalDateTime createdAt) {
        this.id = id;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.note = note;
        this.isRecurring = isRecurring;
        this.recurrenceRule = recurrenceRule;
        this.bench = bench;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public LocalDateTime getAvailableFrom() { return availableFrom; }
    public LocalDateTime getAvailableTo() { return availableTo; }
    public String getNote() { return note; }
    public Boolean getIsRecurring() { return isRecurring; }
    public String getRecurrenceRule() { return recurrenceRule; }
    public LocationDTO getBench() { return bench; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
