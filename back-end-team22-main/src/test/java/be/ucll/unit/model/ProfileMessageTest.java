package be.ucll.unit.model;

import be.ucll.model.CommunicationMessage;
import be.ucll.model.Profile;
import be.ucll.model.ProfileMessage;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileMessageTest {

    @Test
    void givenProfileAndMessage_whenConstructed_thenDefaultsAreSet() {
        Profile profile = new Profile();
        CommunicationMessage msg = new CommunicationMessage();

        ProfileMessage pm = new ProfileMessage(profile, msg);

        assertNotNull(pm.getDeliveredAt(), "deliveredAt should be initialized");
        assertFalse(pm.getRead(), "read should default to false");
        assertNull(pm.getReadAt(), "readAt should be null initially");
        assertEquals(profile, pm.getProfile(), "profile should be set");
        assertEquals(msg, pm.getMessage(), "message should be set");
    }

    @Test
    void whenSettersCalled_thenValuesChange() {
        Profile profile = new Profile();
        CommunicationMessage msg = new CommunicationMessage();
        ProfileMessage pm = new ProfileMessage();

        LocalDateTime now = LocalDateTime.now();

        pm.setProfile(profile);
        pm.setMessage(msg);
        pm.setDeliveredAt(now);
        pm.setRead(true);
        pm.setReadAt(now.plusMinutes(1));

        assertEquals(profile, pm.getProfile());
        assertEquals(msg, pm.getMessage());
        assertEquals(now, pm.getDeliveredAt());
        assertTrue(pm.getRead());
        assertEquals(now.plusMinutes(1), pm.getReadAt());
    }
}
