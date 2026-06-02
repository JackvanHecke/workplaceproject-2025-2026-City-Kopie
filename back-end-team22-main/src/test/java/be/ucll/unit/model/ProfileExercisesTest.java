package be.ucll.unit.model;

import be.ucll.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import static org.junit.jupiter.api.Assertions.*;

class ProfileExercisesTest {

    private ProfileExercises pe;
    private Profile profile;
    private Profile profilewithbmi;
    private Exercise exercise;
    private Location location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profilewithbmi = new Profile(
            "test@example.com",
            0,
            "password123",
            null,
            null,
            null,
            new BigDecimal(0),
            0,
            0,
            0,
            0,
            null,
            null,
            null
        );
        exercise = new Exercise("EX001", "Strength Training", 3, 0, 0, 0);
        location = new Location();
        startTime = LocalDateTime.of(2023, Month.OCTOBER, 15, 9, 30);
        endTime = startTime.plusMinutes(45);
        pe = new ProfileExercises(profile, exercise, location, 45, startTime, endTime, true);
    }

    @Test
    void testDefaultConstructor() {
        ProfileExercises empty = new ProfileExercises();

        assertAll(
            () -> assertNull(empty.getId()),
            () -> assertEquals(0, empty.getDuration()),
            () -> assertNull(empty.getStartedAt()),
            () -> assertEquals(0, empty.getProfileAge())
        );
    }

    @Test
    void testParameterizedConstructorDerivedFields() {
        assertAll(
            () -> assertEquals(0, pe.getProfileAge()),
            () -> assertEquals(null, pe.getProfileGender()),
            () -> assertEquals(null, pe.getProfileCountry()),
            () -> assertEquals(3, pe.getExercise_level()),
            () -> assertEquals(startTime.toLocalDate(), pe.getStartedAtDate()),
            () -> assertEquals(startTime.getDayOfWeek().getValue(), pe.getStartedAtDayOfWeek()),
            () -> assertEquals(9, pe.getStartedAtHour()),
            () -> assertEquals(startTime, pe.getStartedAt()),
            () -> assertEquals(endTime, pe.getFinishedAt()),
            () -> assertEquals(45, pe.getDuration())
        );
    }

    @Test
    void testNullableLocationInConstructor() {
        ProfileExercises noLocation = new ProfileExercises(
            profile, exercise, null, 30, startTime, endTime, true
        );

        assertNull(noLocation.getLocation());
    }

    @Test
    void testGettersAndSetters() {
        pe.setId(1L);
        pe.setDuration(60);
        pe.setStartedAtDayOfWeek(1);
        pe.setStartedAt(startTime.plusDays(1));
        pe.setFinishedAt(endTime.plusDays(1));
        pe.setProfileAgeCategory("30-39");
        pe.setProfileBmiCategory("Normal");

        assertAll(
            () -> assertEquals(1L, pe.getId()),
            () -> assertEquals(60, pe.getDuration()),
            () -> assertEquals(1, pe.getStartedAtDayOfWeek()),
            () -> assertEquals(startTime.plusDays(1), pe.getStartedAt()),
            () -> assertEquals("30-39", pe.getProfileAgeCategory()),
            () -> assertEquals("Normal", pe.getProfileBmiCategory())
        );
    }

    @Test
    void testDateDerivativesUpdateOnStartedAtChange() {
        LocalDateTime newStart = LocalDateTime.of(2024, Month.JANUARY, 1, 15, 0);
        pe.setStartedAt(newStart);

        assertAll(
            () -> assertEquals(LocalDate.of(2023, 10, 15), pe.getStartedAtDate()),
            () -> assertEquals(7, pe.getStartedAtDayOfWeek()), // Monday
            () -> assertEquals(9, pe.getStartedAtHour())
        );
    }

    @Test
    void testNegativeValuesHandling() {
        pe.setDuration(-30);
        pe.setProfileAge(-5);
        pe.setProfileBmi(-10.5f);

        assertAll(
            () -> assertEquals(-30, pe.getDuration()),
            () -> assertEquals(-5, pe.getProfileAge()),
            () -> assertEquals(-10.5f, pe.getProfileBmi())
        );
    }

    @Test
    void testEdgeCaseDateTimeValues() {
        LocalDateTime minTime = LocalDateTime.MIN;
        LocalDateTime maxTime = LocalDateTime.MAX;

        pe.setStartedAt(minTime);
        pe.setFinishedAt(maxTime);

        assertAll(
            () -> assertEquals(LocalDate.of(2023, 10, 15), pe.getStartedAtDate()),
            () -> assertEquals(maxTime, pe.getFinishedAt())
        );
    }

    @Test
    void testStringFieldEdgeCases() {
        String longString = "A".repeat(500);

        pe.setProfileAgeCategory(longString);
        pe.setProfileBmiCategory(longString);
        pe.setProfileCountry(longString.substring(0, 100));

        assertAll(
            () -> assertEquals(longString, pe.getProfileAgeCategory()),
            () -> assertEquals(100, pe.getProfileCountry().length())
        );
    }

    @Test
    void testNullCastConstructor() {
        ProfileExercises nullCast = new ProfileExercises(new Profile(), new Exercise(), new Location(), 0, LocalDate.of(2020, Month.JANUARY, 18).atStartOfDay(), LocalDate.of(2020, Month.JANUARY, 18).atStartOfDay(), false);

        assertAll(
            () -> assertNull(nullCast.getId()),
            () -> assertNotNull(nullCast.getProfile()),
            () -> assertNotNull(nullCast.getExercise()),
            () -> assertNotNull(nullCast.getLocation()),
            () -> assertNotNull(nullCast.getStartedAt()),
            () -> assertNotNull(nullCast.getFinishedAt())
        );
    }

    @Test
    void testSetStartedAt() {
        LocalDate newStart = LocalDate.of(2024, 1, 15);
        pe.setStartedAtDate(newStart);

        assertAll(
            () -> assertEquals(LocalDate.of(2024, 1, 15), pe.getStartedAtDate()),
            () -> assertEquals(7, pe.getStartedAtDayOfWeek()), // Monday
            () -> assertEquals(9, pe.getStartedAtHour())
        );
    }

    @Test
    void testSetStartedAtHour() {
        pe.setStartedAtHour(15);

        assertAll(
            () -> assertEquals(15, pe.getStartedAtHour())
        );
    }

    @Test
    void testSetProfileGender() {
        pe.setProfileGender("M");
        assertEquals("M", pe.getProfileGender());
    }

    @Test
    void setExerciseType() {
        pe.setExerciseTypes("Strength Training");
        assertEquals("Strength Training", pe.getExerciseTypes());
    }

    @Test
    void testProfileBmiInConstructor() {
        ProfileExercises peWithBmi = new ProfileExercises(profilewithbmi, exercise, location, 45, startTime, endTime, true);
        assertEquals(0f, peWithBmi.getProfileBmi(), 0.000001);
    }

    @Test
    void setExerciseLevel() {
        pe.setExercise_level(4);
        assertEquals(4, pe.getExercise_level());
    }

    @Test
    void testNullFieldHandling() {
        pe.setProfileAgeCategory(null);
        pe.setProfileBmiCategory(null);
        pe.setProfileCountry(null);
        pe.setExerciseName(null);

        assertAll(
            () -> assertNull(pe.getProfileAgeCategory()),
            () -> assertNull(pe.getExerciseName())
        );
    }

    @Test
    void testRelationships() {
        Profile newProfile = new Profile();
        Exercise newExercise = new Exercise("EX002", "Cardio", 2, 0, 0, 0);
        Location newLocation = new Location();

        pe.setProfile(newProfile);
        pe.setExercise(newExercise);
        pe.setLocation(newLocation);

        assertAll(
            () -> assertEquals(newProfile, pe.getProfile()),
            () -> assertEquals(newExercise, pe.getExercise()),
            () -> assertEquals(newLocation, pe.getLocation())
        );
    }

    @Test
    void testComparisonWithDateDerivatives() {
        LocalDateTime testTime = LocalDateTime.parse("2023-12-31T23:59:59");
        pe.setStartedAt(testTime);

        assertAll(
            () -> assertEquals(LocalDate.of(2023, 10, 15), pe.getStartedAtDate()),
            () -> assertEquals(7, pe.getStartedAtDayOfWeek()), // Sunday
            () -> assertEquals(9, pe.getStartedAtHour())
        );
    }

    @Test
    void testBmiPrecision() {
        pe.setProfileBmi(24.499999f);
        assertEquals(24.499999f, pe.getProfileBmi(), 0.000001);
    }
}