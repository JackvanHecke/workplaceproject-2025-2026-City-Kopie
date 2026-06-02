package be.ucll.model.dto;

import java.time.LocalDateTime;

public class CoachOfferDTO {
    private Long id;
    private String offerType;
    private String targetGroup;
    private String description;
    private String freeOrPaid; // "free" | "paid"
    private Double price;
    private String recurrence;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private LocalDateTime createdAt;

    public CoachOfferDTO(Long id, String offerType, String targetGroup, String description,
                         String freeOrPaid, Double price, String recurrence,
                         LocalDateTime startDatetime, LocalDateTime endDatetime, LocalDateTime createdAt) {
        this.id = id;
        this.offerType = offerType;
        this.targetGroup = targetGroup;
        this.description = description;
        this.freeOrPaid = freeOrPaid;
        this.price = price;
        this.recurrence = recurrence;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getOfferType() { return offerType; }
    public String getTargetGroup() { return targetGroup; }
    public String getDescription() { return description; }
    public String getFreeOrPaid() { return freeOrPaid; }
    public Double getPrice() { return price; }
    public String getRecurrence() { return recurrence; }
    public LocalDateTime getStartDatetime() { return startDatetime; }
    public LocalDateTime getEndDatetime() { return endDatetime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
