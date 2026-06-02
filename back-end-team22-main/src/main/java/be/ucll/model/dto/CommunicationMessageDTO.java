package be.ucll.model.dto;

import be.ucll.model.enums.CommunicationCategory;
import be.ucll.model.enums.DeliveryChannel;
import be.ucll.model.enums.ProfileRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class CommunicationMessageDTO {

    private Long id;

    // WHAT
    private String title;
    private String body;
    private CommunicationCategory category;

    // HOW
    private Set<DeliveryChannel> channels;

    // WHEN
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private Boolean active;

    // WHO (filters)
    private Integer minAge;
    private Integer maxAge;
    private Set<ProfileRole> targetRoles;

    // META
    private Long createdByProfileId;
    private String createdByName;
    private LocalDateTime createdAt;

    // explicit recipients (optional for detail view)
    private List<CommunicationRecipientDTO> explicitRecipients;

    public CommunicationMessageDTO() {
    }

    public CommunicationMessageDTO(
            Long id,
            String title,
            String body,
            CommunicationCategory category,
            Set<DeliveryChannel> channels,
            LocalDateTime startsAt,
            LocalDateTime endsAt,
            Boolean active,
            Integer minAge,
            Integer maxAge,
            Set<ProfileRole> targetRoles,
            Long createdByProfileId,
            String createdByName,
            LocalDateTime createdAt,
            List<CommunicationRecipientDTO> explicitRecipients
    ) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.category = category;
        this.channels = channels;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.active = active;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.targetRoles = targetRoles;
        this.createdByProfileId = createdByProfileId;
        this.createdByName = createdByName;
        this.createdAt = createdAt;
        this.explicitRecipients = explicitRecipients;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public CommunicationCategory getCategory() { return category; }
    public Set<DeliveryChannel> getChannels() { return channels; }
    public LocalDateTime getStartsAt() { return startsAt; }
    public LocalDateTime getEndsAt() { return endsAt; }
    public Boolean getActive() { return active; }
    public Integer getMinAge() { return minAge; }
    public Integer getMaxAge() { return maxAge; }
    public Set<ProfileRole> getTargetRoles() { return targetRoles; }
    public Long getCreatedByProfileId() { return createdByProfileId; }
    public String getCreatedByName() { return createdByName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<CommunicationRecipientDTO> getExplicitRecipients() { return explicitRecipients; }
}
