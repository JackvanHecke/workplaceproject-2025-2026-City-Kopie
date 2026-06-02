package be.ucll.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STAKEHOLDERS")
public class Stakeholder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Organisation is required.")
    private String organisation;

    @NotBlank(message = "Contact person is required.")
    private String contactPerson;

    @Email(message = "E-mail must be a valid email format.")
    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;

    @NotBlank(message = "Role is required.")
    private String role;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean partnershipDecided = false;

    @ManyToMany
    @JoinTable(
            name = "STAKEHOLDER_LOCATION",
            joinColumns = @JoinColumn(name = "STAKEHOLDER_ID"),
            inverseJoinColumns = @JoinColumn(name = "LOCATION_ID")
    )
    @JsonManagedReference
    private List<Location> locations = new ArrayList<>();

    public Stakeholder() {}

    public Stakeholder(String organisation, String contactPerson, String email, String phoneNumber, String role) {
        this.organisation = organisation;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isPartnershipDecided() {
        return partnershipDecided;
    }

    public void setPartnershipDecided(boolean partnershipDecided) {
        this.partnershipDecided = partnershipDecided;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void addLocation(Location location) {
        locations.add(location);
        location.getStakeholders().add(this);
    }
}
