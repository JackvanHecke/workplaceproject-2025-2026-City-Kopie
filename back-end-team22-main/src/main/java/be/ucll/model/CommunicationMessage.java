package be.ucll.model;

import be.ucll.model.enums.DeliveryChannel;
import be.ucll.model.enums.CommunicationCategory;
import be.ucll.model.enums.ProfileRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COMMUNICATION_MESSAGES")
public class CommunicationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommunicationCategory category = CommunicationCategory.OTHER;

    // --- HOW / WHERE TO SHARE ---

    @ElementCollection(targetClass = DeliveryChannel.class)
    @CollectionTable(
            name = "COMMUNICATION_MESSAGE_CHANNELS",
            joinColumns = @JoinColumn(name = "message_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private Set<DeliveryChannel> channels = new HashSet<>();

    // --- WHEN TO SHARE ---

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at")
    private LocalDateTime endsAt; // can be null => open-ended

    @Column(name = "is_active", nullable = false)
    private Boolean active = Boolean.TRUE;

    // --- WHO CREATED IT (CLIENT / OWNER) ---

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by_profile_id", nullable = false)
    private Profile createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- TARGETING: FILTERS (age / role) ---

    @Column(name = "min_age")
    private Integer minAge; // optional

    @Column(name = "max_age")
    private Integer maxAge; // optional

    @ElementCollection(targetClass = ProfileRole.class)
    @CollectionTable(
            name = "COMMUNICATION_TARGET_ROLES",
            joinColumns = @JoinColumn(name = "message_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<ProfileRole> targetRoles = new HashSet<>();
    // e.g. [COACH], [END_USER], [END_USER, COACH], etc.

    // --- TARGETING: EXPLICIT SELECTED PROFILES ---

    @ManyToMany
    @JoinTable(
            name = "COMMUNICATION_EXPLICIT_RECIPIENTS",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private Set<Profile> explicitRecipients = new HashSet<>();

    // ===== GETTERS / SETTERS =====

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public CommunicationCategory getCategory() { return category; }
    public void setCategory(CommunicationCategory category) { this.category = category; }

    public Set<DeliveryChannel> getChannels() { return channels; }
    public void setChannels(Set<DeliveryChannel> channels) { this.channels = channels; }

    public LocalDateTime getStartsAt() { return startsAt; }
    public void setStartsAt(LocalDateTime startsAt) { this.startsAt = startsAt; }

    public LocalDateTime getEndsAt() { return endsAt; }
    public void setEndsAt(LocalDateTime endsAt) { this.endsAt = endsAt; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Profile getCreatedBy() { return createdBy; }
    public void setCreatedBy(Profile createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }

    public Integer getMaxAge() { return maxAge; }
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }

    public Set<ProfileRole> getTargetRoles() { return targetRoles; }
    public void setTargetRoles(Set<ProfileRole> targetRoles) { this.targetRoles = targetRoles; }

    public Set<Profile> getExplicitRecipients() { return explicitRecipients; }
    public void setExplicitRecipients(Set<Profile> explicitRecipients) { this.explicitRecipients = explicitRecipients; }
}
