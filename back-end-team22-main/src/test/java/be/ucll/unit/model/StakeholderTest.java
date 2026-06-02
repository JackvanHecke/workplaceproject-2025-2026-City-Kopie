package be.ucll.unit.model;

import be.ucll.model.Stakeholder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StakeholderTest {

    private Stakeholder stakeholder;
    @SuppressWarnings("unused")
    private Stakeholder stakeholder2;

    @BeforeEach
    void setUp() {
        stakeholder2 = new Stakeholder(
            
        );
        stakeholder = new Stakeholder(
            "City Health Department",
            "John Smith",
            "john.health@city.gov",
            "+3212345678",
            "Municipal Advisor"
        );
    }

    @Test
    void testProtectedConstructor() {
        Stakeholder empty = new Stakeholder(null, null, null, null, null);
        empty.setId(-1L);
        assertNull(empty.getOrganisation());
        assertNull(empty.getContactPerson());
        assertNull(empty.getEmail());
        assertNull(empty.getPhoneNumber());
        assertNull(empty.getRole());
    }

    @Test
    void testParameterizedConstructor() {
        assertAll(
            () -> assertEquals("City Health Department", stakeholder.getOrganisation()),
            () -> assertEquals("John Smith", stakeholder.getContactPerson()),
            () -> assertEquals("john.health@city.gov", stakeholder.getEmail()),
            () -> assertEquals("+3212345678", stakeholder.getPhoneNumber()),
            () -> assertEquals("Municipal Advisor", stakeholder.getRole())
        );
    }

    @Test
    void testGettersAndSetters() {
        stakeholder.setId(1L);
        stakeholder.setOrganisation("Regional Sports Council");
        stakeholder.setContactPerson("Sarah Johnson");
        stakeholder.setEmail("sarah.sports@region.org");
        stakeholder.setPhoneNumber("+3298765432");
        stakeholder.setRole("Funding Coordinator");

        assertAll(
            () -> assertEquals(1L, stakeholder.getId()),
            () -> assertEquals("Regional Sports Council", stakeholder.getOrganisation()),
            () -> assertEquals("Sarah Johnson", stakeholder.getContactPerson()),
            () -> assertEquals("sarah.sports@region.org", stakeholder.getEmail()),
            () -> assertEquals("+3298765432", stakeholder.getPhoneNumber()),
            () -> assertEquals("Funding Coordinator", stakeholder.getRole())
        );
    }

    @Test
    void testNullAndEmptyFieldHandling() {
        stakeholder.setOrganisation("");
        stakeholder.setContactPerson(null);
        stakeholder.setEmail("");
        stakeholder.setPhoneNumber("  ");
        stakeholder.setRole(null);

        assertAll(
            () -> assertEquals("", stakeholder.getOrganisation()),
            () -> assertNull(stakeholder.getContactPerson()),
            () -> assertEquals("", stakeholder.getEmail()),
            () -> assertEquals("  ", stakeholder.getPhoneNumber()),
            () -> assertNull(stakeholder.getRole())
        );
    }

    @Test
    void testEdgeCaseLongStrings() {
        String longString = "A".repeat(500);

        stakeholder.setOrganisation(longString);
        stakeholder.setContactPerson(longString);
        stakeholder.setEmail(longString + "@domain.com");
        stakeholder.setPhoneNumber(longString.substring(0, 50));
        stakeholder.setRole(longString);

        assertAll(
            () -> assertEquals(longString, stakeholder.getOrganisation()),
            () -> assertEquals(longString, stakeholder.getContactPerson()),
            () -> assertTrue(stakeholder.getEmail().endsWith("@domain.com")),
            () -> assertEquals(50, stakeholder.getPhoneNumber().length()),
            () -> assertEquals(longString, stakeholder.getRole())
        );
    }

    @Test
    void testIdGeneration() {
        assertNull(stakeholder.getId());
        stakeholder.setId(1000L);
        assertEquals(1000L, stakeholder.getId());
    }

    @Test
    void testPhoneNumberFormats() {
        stakeholder.setPhoneNumber("123-456-789");
        assertEquals("123-456-789", stakeholder.getPhoneNumber());

        stakeholder.setPhoneNumber("+32.478.123.456");
        assertEquals("+32.478.123.456", stakeholder.getPhoneNumber());

        stakeholder.setPhoneNumber("0044123456789");
        assertEquals("0044123456789", stakeholder.getPhoneNumber());
    }
}