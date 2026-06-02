package be.ucll.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import be.ucll.model.enums.ProfileRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "PROFILES")
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String email;

    @Column(name = "GENDER")
    private String gender;

    private String password;

    @Column(name = "NATIONALITY")
    private String nationality;

    @Column(name = "BMI", precision = 4, scale = 1)
    private BigDecimal bmi;

    @Column(name = "performed_exercises")
    private int performedExercises;

    @Column(name = "performed_training_sessions")
    private int performedTrainingSessions;

    @Column(name = "number_of_benches")
    private int numberOfBenches;

    @Column(name = "movement_minutes")
    private int movementMinutes;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    // @OneToMany(mappedBy = "profile")
    // @JsonManagedReference("profile-completion")
    // private Set<CompletedChecklistItem> completedChecklistItems = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    private Set<ProfileExercises> profileExercises = new HashSet<>();

    @Column(name = "SALT")
    private String salt;

    @Column(name = "PASSWORDHASH")
    private String passwordHash;

    @OneToMany
    private Set<AuthToken> validTokens = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private ProfileRole role = ProfileRole.END_USER;

    @OneToMany(mappedBy = "createdBy")
    private Set<CommunicationMessage> createdMessages = new HashSet<>();

    @Column(name = "notifications_enabled")
    private Boolean notificationsEnabled = Boolean.TRUE;

    @OneToMany(mappedBy = "profile")
    private Set<ProfileMessage> inboxItems = new HashSet<>();

    public Profile() {
    }

    public Profile(
            String name,
            int age,
            String email,
            String gender,
            String password,
            String nationality,
            BigDecimal bmi,
            int performedExercises,
            int performedTrainingSessions,
            int numberOfBenches,
            int movementMinutes,
            LocalDateTime registeredAt,
            String salt,
            String passwordHash) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.password = password;
        this.nationality = nationality;
        this.bmi = bmi;
        this.performedExercises = performedExercises;
        this.performedTrainingSessions = performedTrainingSessions;
        this.numberOfBenches = numberOfBenches;
        this.movementMinutes = movementMinutes;
        this.registeredAt = (registeredAt != null) ? registeredAt : LocalDateTime.now();
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    public Profile(
            String name,
            int age,
            String email,
            String gender,
            String password,
            String nationality,
            BigDecimal bmi,
            int performedExercises,
            int performedTrainingSessions,
            int numberOfBenches,
            int movementMinutes,
            String salt,
            String passwordHash) {
        this(name, age, email, gender, password, nationality, bmi,
                performedExercises, performedTrainingSessions, numberOfBenches,
                movementMinutes, LocalDateTime.now(), salt, passwordHash);
    }

    // public Set<CompletedChecklistItem> getCompletedChecklistItems() {
    //     return completedChecklistItems;
    // }

    // public void setCompletedChecklistItems(Set<CompletedChecklistItem> completedChecklistItems) {
    //     this.completedChecklistItems = completedChecklistItems;
    // }

    public Long getId() {
        return id;
    }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public BigDecimal getBmi() { return bmi; }
    public void setBmi(BigDecimal bmi) { this.bmi = bmi; }

    public int getPerformedExercises() { return performedExercises; }
    public void setPerformedExercises(int performedExercises) { this.performedExercises = performedExercises; }

    public int getPerformedTrainingSessions() { return performedTrainingSessions; }
    public void setPerformedTrainingSessions(int performedTrainingSessions) { this.performedTrainingSessions = performedTrainingSessions; }

    public int getNumberOfBenches() { return numberOfBenches; }
    public void setNumberOfBenches(int numberOfBenches) { this.numberOfBenches = numberOfBenches; }

    public int getMovementMinutes() { return movementMinutes; }
    public void setMovementMinutes(int movementMinutes) { this.movementMinutes = movementMinutes; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }

    public Set<ProfileExercises> getProfileExercises() { return profileExercises; }
    public void setProfileExercises(Set<ProfileExercises> profileExercises) { this.profileExercises = profileExercises; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Set<AuthToken> getValidTokens() { return validTokens; }
    public void setValidTokens(Set<AuthToken> validTokens) { this.validTokens = validTokens; }
    public void addValidToken(AuthToken authToken) { this.validTokens.add(authToken); }
    public void removeValidToken(AuthToken authToken) { this.validTokens.remove(authToken); }

    public ProfileRole getRole() { return role; }
    public void setRole(ProfileRole role) { this.role = role; }

    public Set<CommunicationMessage> getCreatedMessages() { return createdMessages; }
    public void setCreatedMessages(Set<CommunicationMessage> createdMessages) { this.createdMessages = createdMessages; }

    public Boolean getNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(Boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }

    public Set<ProfileMessage> getInboxItems() { return inboxItems; }
    public void setInboxItems(Set<ProfileMessage> inboxItems) { this.inboxItems = inboxItems; }
}
