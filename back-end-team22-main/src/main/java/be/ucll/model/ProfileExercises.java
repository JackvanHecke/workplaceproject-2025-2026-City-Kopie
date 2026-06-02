package be.ucll.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "PROFILE_EXERCISES")
public class ProfileExercises {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int duration;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "started_at_date")
    private LocalDate startedAtDate;

    @Column(name = "started_at_day_of_week")
    private int startedAtDayOfWeek;

    @Column(name = "started_at_hour")
    private int startedAtHour;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name = "profile_age")
    private int profileAge;

    @Column(name = "profile_age_category")
    private String profileAgeCategory;

    @Column(name = "profile_gender")
    private String profileGender;

    @Column(name = "profile_country")
    private String profileCountry;

    @Column(name = "profile_bmi")
    private float profileBmi;

    @Column(name = "profile_bmi_category")
    private String profileBmiCategory;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "exercise_name")
    private String exerciseName;

    @Column(name = "exercise_types")
    private String exerciseTypes;

    @Column(name = "exercise_level")
    private int exercise_level;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    public ProfileExercises() {}

    public ProfileExercises(Profile profile, Exercise exercise, Location location,
                            int duration, LocalDateTime startedAt, LocalDateTime finishedAt, boolean completed) {
        this.profile = profile;
        this.exercise = exercise;
        this.location = location;
        this.duration = duration;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.profileAge = profile.getAge();
        this.profileGender = profile.getGender();
        this.profileCountry = profile.getNationality();
        this.profileBmi = profile.getBmi() == null? 0 : profile.getBmi().floatValue();
        this.exercise_level = exercise.getExercise_level();
        this.exerciseTypes = exercise.getExercise_type();
        this.startedAtDate = startedAt.toLocalDate();
        this.startedAtDayOfWeek = startedAt.getDayOfWeek().getValue();
        this.startedAtHour = startedAt.getHour();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDate getStartedAtDate() {
        return startedAtDate;
    }

    public void setStartedAtDate(LocalDate startedAtDate) {
        this.startedAtDate = startedAtDate;
    }

    public int getStartedAtDayOfWeek() {
        return startedAtDayOfWeek;
    }

    public void setStartedAtDayOfWeek(int startedAtDayOfWeek) {
        this.startedAtDayOfWeek = startedAtDayOfWeek;
    }

    public int getStartedAtHour() {
        return startedAtHour;
    }

    public void setStartedAtHour(int startedAtHour) {
        this.startedAtHour = startedAtHour;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public int getProfileAge() {
        return profileAge;
    }

    public void setProfileAge(int profileAge) {
        this.profileAge = profileAge;
    }

    public String getProfileAgeCategory() {
        return profileAgeCategory;
    }

    public void setProfileAgeCategory(String profileAgeCategory) {
        this.profileAgeCategory = profileAgeCategory;
    }

    public String getProfileGender() {
        return profileGender;
    }

    public void setProfileGender(String profileGender) {
        this.profileGender = profileGender;
    }

    public String getProfileCountry() {
        return profileCountry;
    }

    public void setProfileCountry(String profileCountry) {
        this.profileCountry = profileCountry;
    }

    public float getProfileBmi() {
        return profileBmi;
    }

    public void setProfileBmi(float profileBmi) {
        this.profileBmi = profileBmi;
    }

    public String getProfileBmiCategory() {
        return profileBmiCategory;
    }

    public void setProfileBmiCategory(String profileBmiCategory) {
        this.profileBmiCategory = profileBmiCategory;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseTypes() {
        return exerciseTypes;
    }

    public void setExerciseTypes(String exerciseTypes) {
        this.exerciseTypes = exerciseTypes;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getExercise_level() {
        return exercise_level;
    }

    public void setExercise_level(int exercise_level) {
        this.exercise_level = exercise_level;
    }

}
