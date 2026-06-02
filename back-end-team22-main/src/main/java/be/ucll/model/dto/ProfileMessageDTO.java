// be/ucll/model/dto/ProfileMessageDTO.java
package be.ucll.model.dto;

import be.ucll.model.enums.DeliveryChannel;

import java.time.LocalDateTime;
import java.util.Set;

public class ProfileMessageDTO {

    private Long id;
    private Long messageId;
    private String title;
    private String preview;
    private Boolean read;
    private LocalDateTime deliveredAt;

    // ➜ NEW
    private Set<DeliveryChannel> channels;

    public ProfileMessageDTO() {}

    public ProfileMessageDTO(
            Long id,
            Long messageId,
            String title,
            String preview,
            Boolean read,
            LocalDateTime deliveredAt,
            Set<DeliveryChannel> channels
    ) {
        this.id = id;
        this.messageId = messageId;
        this.title = title;
        this.preview = preview;
        this.read = read;
        this.deliveredAt = deliveredAt;
        this.channels = channels;
    }

    public Long getId() { return id; }
    public Long getMessageId() { return messageId; }
    public String getTitle() { return title; }
    public String getPreview() { return preview; }
    public Boolean getRead() { return read; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public Set<DeliveryChannel> getChannels() { return channels; }

    public void setId(Long id) { this.id = id; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }
    public void setTitle(String title) { this.title = title; }
    public void setPreview(String preview) { this.preview = preview; }
    public void setRead(Boolean read) { this.read = read; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
    public void setChannels(Set<DeliveryChannel> channels) { this.channels = channels; }
}
