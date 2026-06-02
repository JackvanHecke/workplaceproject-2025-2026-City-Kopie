package be.ucll.model.dto;

import be.ucll.model.enums.CommunicationCategory;
import be.ucll.model.enums.DeliveryChannel;
import be.ucll.model.enums.ProfileRole;

import java.time.LocalDateTime;
import java.util.Set;

public class CommunicationMessageCreateDTO {

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

    // WHO (explicit profiles)
    private Set<Long> explicitProfileIds;

    // Who creates this message (for now, pass from frontend)
    private Long createdByProfileId;

    public CommunicationMessageCreateDTO() {
    }

    // Getters & setters

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

    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }

    public Integer getMaxAge() { return maxAge; }
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }

    public Set<ProfileRole> getTargetRoles() { return targetRoles; }
    public void setTargetRoles(Set<ProfileRole> targetRoles) { this.targetRoles = targetRoles; }

    public Set<Long> getExplicitProfileIds() { return explicitProfileIds; }
    public void setExplicitProfileIds(Set<Long> explicitProfileIds) { this.explicitProfileIds = explicitProfileIds; }

    public Long getCreatedByProfileId() { return createdByProfileId; }
    public void setCreatedByProfileId(Long createdByProfileId) { this.createdByProfileId = createdByProfileId; }
}
