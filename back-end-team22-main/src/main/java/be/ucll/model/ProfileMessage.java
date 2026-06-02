package be.ucll.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PROFILE_MESSAGES")
public class ProfileMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // WHO: receiver
    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    // WHAT: original communication
    @ManyToOne(optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private CommunicationMessage message;

    @Column(name = "delivered_at", nullable = false)
    private LocalDateTime deliveredAt = LocalDateTime.now();

    @Column(name = "is_read", nullable = false)
    private Boolean read = Boolean.FALSE;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    public ProfileMessage() {}

    public ProfileMessage(Profile profile, CommunicationMessage message) {
        this.profile = profile;
        this.message = message;
        this.deliveredAt = LocalDateTime.now();
        this.read = Boolean.FALSE;
    }

    public Long getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public CommunicationMessage getMessage() {
        return message;
    }

    public void setMessage(CommunicationMessage message) {
        this.message = message;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}
