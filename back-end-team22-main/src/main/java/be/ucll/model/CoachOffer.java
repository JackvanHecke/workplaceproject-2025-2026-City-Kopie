package be.ucll.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COACH_OFFERS")
public class CoachOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // owning side
    @ManyToOne(optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @ManyToOne
    @JoinColumn(name = "bench_id")
    private Location bench; // optional

    @Column(name = "offer_type")
    private String offerType;

    @Column(name = "target_group")
    private String targetGroup;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "free_or_paid")
    @Enumerated(EnumType.STRING)
    private FeeType freeOrPaid = FeeType.paid;

    private Double price;

    private String recurrence;

    @Column(name = "start_datetime")
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum FeeType { free, paid }

    public CoachOffer() {}

    // getters/setters
    public Long getId() { return id; }
    public Coach getCoach() { return coach; }
    public void setCoach(Coach coach) { this.coach = coach; }
    public Location getBench() { return bench; }
    public void setBench(Location bench) { this.bench = bench; }
    public String getOfferType() { return offerType; }
    public void setOfferType(String offerType) { this.offerType = offerType; }
    public String getTargetGroup() { return targetGroup; }
    public void setTargetGroup(String targetGroup) { this.targetGroup = targetGroup; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public FeeType getFreeOrPaid() { return freeOrPaid; }
    public void setFreeOrPaid(FeeType freeOrPaid) { this.freeOrPaid = freeOrPaid; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getRecurrence() { return recurrence; }
    public void setRecurrence(String recurrence) { this.recurrence = recurrence; }
    public LocalDateTime getStartDatetime() { return startDatetime; }
    public void setStartDatetime(LocalDateTime startDatetime) { this.startDatetime = startDatetime; }
    public LocalDateTime getEndDatetime() { return endDatetime; }
    public void setEndDatetime(LocalDateTime endDatetime) { this.endDatetime = endDatetime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
