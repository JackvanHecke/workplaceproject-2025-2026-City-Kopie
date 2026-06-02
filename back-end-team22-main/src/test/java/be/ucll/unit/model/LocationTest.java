package be.ucll.unit.model;

import be.ucll.model.Location;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void testDefaultConstructor() {
        Location location = new Location();

        // Basic fields
        assertNull(location.getBenchId());
        assertNull(location.getBenchName());
        assertNull(location.getBenchOwner());
        // Add similar assertions for other basic fields...
    }

    @Test
    void testParameterizedConstructorAndGetters() {
        // Create sample data
        Location location = new Location(
            1L, "Main Bench", "City Parks", "Brussels", "Belgium",
            "Large", "Public", 5,
            101L, 102L, 103L, 104L, 105L, 106L, 107L, 108L, 109L, 110L,  // Top 10 exercises
            100, 101, 102, 103, 104, 105, 106,  // Weekly exercises
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9,  // Hourly exercises (00-09)
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19,  // Hourly exercises (10-19)
            20, 21, 22, 23,  // Hourly exercises (20-23)
            500, 600,  // Gender
            50, 100, 150, 200, 250, 300, 350,  // Age groups
            40, 150, 200, 180, 130,  // BMI groups
            12000, "mixed", "park,outdoor,free"
        );

        // Basic fields
        assertEquals(1L, location.getBenchId());
        assertEquals("Main Bench", location.getBenchName());
        assertEquals("City Parks", location.getBenchOwner());
        
        // Top 10 exercises
        assertEquals(101L, location.getTop10_1_exercise_id());
        assertEquals(102L, location.getTop10_2_exercise_id());
        assertEquals(103L, location.getTop10_3_exercise_id());
        // Continue for all 10 exercise IDs...
        
        // Weekly exercises
        assertEquals(100, location.getExercises_per_day_monday());
        assertEquals(101, location.getExercises_per_day_tuesday());
        // Continue for all days...
        
        // Hourly exercises
        assertEquals(0, location.getExercise_per_hour_00());
        assertEquals(1, location.getExercise_per_hour_01());
        assertEquals(2, location.getExercise_per_hour_02());
        // Continue for all 24 hours...
        
        // Gender stats
        assertEquals(500, location.getExercises_gender_m());
        assertEquals(600, location.getExercises_gender_f());
        
        // Age groups
        assertEquals(50, location.getExercises_age_0_17());
        assertEquals(100, location.getExercises_age_18_24());
        // Continue for all age groups...
        
        // BMI groups
        assertEquals(40, location.getExercises_bmi_0_18_5());
        assertEquals(150, location.getExercises_bmi_18_5_25());
        // Continue for all BMI groups...
        
        // Additional fields
        assertEquals(12000, location.getMovement_minutes());
        assertEquals("mixed", location.getGender_group());
        assertEquals("park,outdoor,free", location.getTags());
    }

    @Test
    void testSettersAndGetters() {
        Location location = new Location();
        
        // Basic fields
        location.setBenchId(2L);
        location.setBenchName("Central Park Bench");
        location.setBenchOwner("NYC Parks Dept");
        
        assertEquals(2L, location.getBenchId());
        assertEquals("Central Park Bench", location.getBenchName());
        assertEquals("NYC Parks Dept", location.getBenchOwner());
        
        // Top 10 exercises
        location.setTop10_1_exercise_id(201L);
        location.setTop10_2_exercise_id(202L);
        // Set and verify all top 10 exercise IDs...
        
        // Weekly exercises
        location.setExercises_per_day_monday(200);
        location.setExercises_per_day_tuesday(201);
        assertEquals(200, location.getExercises_per_day_monday());
        assertEquals(201, location.getExercises_per_day_tuesday());
        // Continue for all days...
        
        // Hourly exercises
        location.setExercise_per_hour_00(10);
        location.setExercise_per_hour_01(11);
        assertEquals(10, location.getExercise_per_hour_00());
        assertEquals(11, location.getExercise_per_hour_01());
        // Continue for all hours...
        
        // Negative values test
        location.setExercises_per_day_sunday(-5);
        location.setExercise_per_hour_23(-10);
        assertEquals(-5, location.getExercises_per_day_sunday());
        assertEquals(-10, location.getExercise_per_hour_23());
        
        // Demographic groups
        location.setExercises_gender_m(300);
        location.setExercises_gender_f(400);
        assertEquals(300, location.getExercises_gender_m());
        assertEquals(400, location.getExercises_gender_f());
        
        // Edge cases
        location.setConnectedRoutes(Integer.MAX_VALUE);
        location.setMovement_minutes(Integer.MIN_VALUE);
        assertEquals(Integer.MAX_VALUE, location.getConnectedRoutes());
        assertEquals(Integer.MIN_VALUE, location.getMovement_minutes());
        
        // Null handling
        location.setBenchName(null);
        location.setGender_group(null);
        assertNull(location.getBenchName());
        assertNull(location.getGender_group());
    }

    @Test
    void testEdgeCases() {
        Location location = new Location();
        
        // Max/Min values
        location.setBenchId(Long.MAX_VALUE);
        location.setConnectedRoutes(Integer.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, location.getBenchId());
        assertEquals(Integer.MAX_VALUE, location.getConnectedRoutes());
        
        // Empty/NULL strings
        location.setBenchCity("");
        location.setBenchCountry(null);
        assertEquals("", location.getBenchCity());
        assertNull(location.getBenchCountry());

        // Extreme integer values
        for (int i = 0; i < 24; i++) {
            String method = "setExercise_per_hour_" + String.format("%02d", i);
            switch(method) {
                case "setExercise_per_hour_00":
                    location.setExercise_per_hour_00(Integer.MIN_VALUE);
                    assertEquals(Integer.MIN_VALUE, location.getExercise_per_hour_00());
                    break;
                // Create similar cases for all 24 hours...
            }
        }
    }

    @Test
    void testToString() {
        Location location = new Location();
        location.setBenchId(2L);
        location.setBenchName("Central Park Bench");
        location.setBenchOwner("NYC Parks Dept");

        assertNotNull(location.toString());
    }

    @Test
    void testExercisePerHour()
    {
        Location location = new Location();

        for (int i = 0; i < 24; i++) {
            location.setExercise_per_hour(i, 10);
            assertEquals(10, location.getExercise_per_hour(i));
        }
    }

    @Test
    void testExercisePerHourDefaultCase()
    {
        Location location = new Location();
        assertThrows(IllegalArgumentException.class, () -> location.setExercise_per_hour(25, 10));
        assertThrows(IllegalArgumentException.class, () -> location.getExercise_per_hour(25));
    }

    @Test
    void testSetTags()
    {
        Location location = new Location();
        location.setTags("tag1,tag2,tag3");
        assertEquals("tag1,tag2,tag3", location.getTags());
    }

    @Test
    void testBenchSize()
    {
        Location location = new Location();
        location.setBenchSize("Large");
        assertEquals("Large", location.getBenchSize());
    }

    @Test
    void testBenchType()
    {
        Location location = new Location();
        location.setBenchType("Park");
        assertEquals("Park", location.getBenchType());
    }

    @Test
    void testExercisePerDay()
    {
        Location location = new Location();
        location.setExercises_per_day_monday(10);
        assertEquals(10, location.getExercises_per_day_monday());
        location.setExercises_per_day_tuesday(20);
        assertEquals(20, location.getExercises_per_day_tuesday());
        location.setExercises_per_day_wednesday(30);
        assertEquals(30, location.getExercises_per_day_wednesday());
        location.setExercises_per_day_thursday(40);
        assertEquals(40, location.getExercises_per_day_thursday());
        location.setExercises_per_day_friday(50);
        assertEquals(50, location.getExercises_per_day_friday());
        location.setExercises_per_day_saturday(60);
        assertEquals(60, location.getExercises_per_day_saturday());
        location.setExercises_per_day_sunday(70);
        assertEquals(70, location.getExercises_per_day_sunday());
    }

    @Test
    void testExercisePerAgeGroup()
    {
        Location location = new Location();
        location.setExercises_age_0_17(10);
        assertEquals(10, location.getExercises_age_0_17());
        location.setExercises_age_18_24(20);
        assertEquals(20, location.getExercises_age_18_24());
        location.setExercises_age_25_34(30);
        assertEquals(30, location.getExercises_age_25_34());
        location.setExercises_age_35_44(40);
        assertEquals(40, location.getExercises_age_35_44());
        location.setExercises_age_45_54(50);
        assertEquals(50, location.getExercises_age_45_54());
        location.setExercises_age_55_64(60);
        assertEquals(60, location.getExercises_age_55_64());
        location.setExercises_age_65_plus(70);
        assertEquals(70, location.getExercises_age_65_plus());
    }

    @Test
    void testExercisePerBMIGroup()
    {
        Location location = new Location();
        location.setExercises_bmi_0_18_5(10);
        assertEquals(10, location.getExercises_bmi_0_18_5());
        location.setExercises_bmi_18_5_25(20);
        assertEquals(20, location.getExercises_bmi_18_5_25());
        location.setExercises_bmi_25_30(30);
        assertEquals(30, location.getExercises_bmi_25_30());
        location.setExercises_bmi_30_35(40);
        assertEquals(40, location.getExercises_bmi_30_35());
        location.setExercises_bmi_35_plus(50);
        assertEquals(50, location.getExercises_bmi_35_plus());
    }

    @Test
    void testGetTopExercises()
    {
        Location location = new Location();
        location.setTop10_1_exercise_id(1L);
        location.setTop10_2_exercise_id(2L);
        location.setTop10_3_exercise_id(3L);
        location.setTop10_4_exercise_id(4L);
        location.setTop10_5_exercise_id(5L);
        location.setTop10_6_exercise_id(6L);
        location.setTop10_7_exercise_id(7L);
        location.setTop10_8_exercise_id(8L);
        location.setTop10_9_exercise_id(9L);
        location.setTop10_10_exercise_id(10L);

        assertEquals(1L, location.getTop10_1_exercise_id());
        assertEquals(2L, location.getTop10_2_exercise_id());
        assertEquals(3L, location.getTop10_3_exercise_id());
        assertEquals(4L, location.getTop10_4_exercise_id());
        assertEquals(5L, location.getTop10_5_exercise_id());
        assertEquals(6L, location.getTop10_6_exercise_id());
        assertEquals(7L, location.getTop10_7_exercise_id());
        assertEquals(8L, location.getTop10_8_exercise_id());
        assertEquals(9L, location.getTop10_9_exercise_id());
        assertEquals(10L, location.getTop10_10_exercise_id());
    }
}