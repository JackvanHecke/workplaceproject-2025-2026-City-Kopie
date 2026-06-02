package be.ucll.model.dto;

public class CoachUpsertRequest {
    private Long profileId; // required for POST, forbidden for PUT
    private String bio;
    private Boolean isAvailable;
    private Boolean notifyNewDevices;
    private Integer defaultRadiusKm;

    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) { this.profileId = profileId; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public Boolean getNotifyNewDevices() { return notifyNewDevices; }
    public void setNotifyNewDevices(Boolean notifyNewDevices) { this.notifyNewDevices = notifyNewDevices; }

    public Integer getDefaultRadiusKm() { return defaultRadiusKm; }
    public void setDefaultRadiusKm(Integer defaultRadiusKm) { this.defaultRadiusKm = defaultRadiusKm; }
}
