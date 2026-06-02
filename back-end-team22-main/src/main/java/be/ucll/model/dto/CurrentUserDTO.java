package be.ucll.model.dto;

import be.ucll.model.enums.ProfileRole; // Import the enum
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CurrentUserDTO {
    private Long id;
    private String name;
    private String email;
    private String nationality;
    private Integer age;
    private String gender;
    private BigDecimal bmi;
    private int movementMinutes;
    private LocalDateTime registeredAt;
    private ProfileRole role;

    public CurrentUserDTO() {
    }

    public CurrentUserDTO(Long id, String name, String email, String nationality, Integer age, String gender,
            BigDecimal bmi, int movementMinutes, LocalDateTime registeredAt, ProfileRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nationality = nationality;
        this.age = age;
        this.gender = gender;
        this.bmi = bmi;
        this.movementMinutes = movementMinutes;
        this.registeredAt = registeredAt;
        this.role = role;
    }

    public ProfileRole getRole() {
        return role;
    }

    public void setRole(ProfileRole role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public BigDecimal getBmi() {
        return bmi;
    }

    public void setBmi(BigDecimal bmi) {
        this.bmi = bmi;
    }

    public int getMovementMinutes() {
        return movementMinutes;
    }

    public void setMovementMinutes(int movementMinutes) {
        this.movementMinutes = movementMinutes;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
}