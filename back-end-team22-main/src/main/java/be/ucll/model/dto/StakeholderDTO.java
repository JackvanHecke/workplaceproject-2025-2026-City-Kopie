package be.ucll.model.dto;

public class StakeholderDTO {

    private Long id;
    private String organisation;
    private String contactPerson;
    private String email;
    private String phoneNumber;
    private String role;
    private Boolean partnershipDecided;

    public StakeholderDTO() {}

    public StakeholderDTO(Long id, String organisation, String contactPerson, String email,
                          String phoneNumber, String role, Boolean partnershipDecided) {
        this.id = id;
        this.organisation = organisation;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.partnershipDecided = partnershipDecided;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrganisation() { return organisation; }
    public void setOrganisation(String organisation) { this.organisation = organisation; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getPartnershipDecided() { return partnershipDecided; }
    public void setPartnershipDecided(Boolean partnershipDecided) { this.partnershipDecided = partnershipDecided; }
}
