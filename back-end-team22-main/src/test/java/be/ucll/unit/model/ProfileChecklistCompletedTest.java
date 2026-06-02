package be.ucll.unit.model;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.model.Checklist;
import be.ucll.model.Profile;
import be.ucll.model.ProfileChecklistCompleted;


public class ProfileChecklistCompletedTest {

    private ProfileChecklistCompleted profileChecklistCompleted;

    @BeforeEach
    void setUp() {
        profileChecklistCompleted = new ProfileChecklistCompleted();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        profileChecklistCompleted.setId(id);
        assertEquals(id, profileChecklistCompleted.getId());
    }

    @Test
    void testProfile() {
        Profile profile = new Profile();
        profileChecklistCompleted.setProfile(profile);
        assertEquals(profile, profileChecklistCompleted.getProfile());
    }

    @Test
    void testChecklist() {
        Checklist checklist = new Checklist();
        profileChecklistCompleted.setChecklist(checklist);
        assertEquals(checklist, profileChecklistCompleted.getChecklist());
    }

    @Test
    void testCompletedAt() {
        java.time.LocalDateTime completedAt = java.time.LocalDateTime.now();
        profileChecklistCompleted.setCompletedAt(completedAt);
        assertEquals(completedAt, profileChecklistCompleted.getCompletedAt());
    }
}
