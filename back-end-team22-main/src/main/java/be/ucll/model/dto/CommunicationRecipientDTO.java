package be.ucll.model.dto;

import be.ucll.model.enums.ProfileRole;

public class CommunicationRecipientDTO {

    private Long id;
    private String name;
    private ProfileRole role;

    public CommunicationRecipientDTO() {
    }

    public CommunicationRecipientDTO(Long id, String name, ProfileRole role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public ProfileRole getRole() { return role; }
}
