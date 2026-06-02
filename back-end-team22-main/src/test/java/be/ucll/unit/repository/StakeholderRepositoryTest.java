package be.ucll.unit.repository;

import be.ucll.model.Stakeholder;
import be.ucll.repository.StakeholderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StakeholderRepositoryTest {

    @Autowired
    private StakeholderRepository repository;

    private Stakeholder stakeholder1;
    private Stakeholder stakeholder2;

    @BeforeEach
    public void setup() {
        repository.deleteAll(); // clear DB before each test

        stakeholder1 = new Stakeholder("UCLL", "John Doe", "john.doe@ucll.be", "0499123456", "Partner");
        stakeholder2 = new Stakeholder("Telenet", "Jane Smith", "jane.smith@telenet.be", "0478123456", "Sponsor");

        repository.save(stakeholder1);
        repository.save(stakeholder2);
    }

    @Test
    public void testFindAll() {
        List<Stakeholder> all = repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    public void testSave() {
        Stakeholder s = new Stakeholder("Google", "Sundar P", "sundar@google.com", "0455000000", "Partner");
        repository.save(s);

        List<Stakeholder> all = repository.findAll();
        assertEquals(3, all.size());
        assertTrue(all.contains(s));
    }

    @Test
    public void testFindStakeholderById() {
        Stakeholder found = repository.findStakeholderById(stakeholder1.getId());
        assertNotNull(found);
        assertEquals("john.doe@ucll.be", found.getEmail());
    }

    @Test
    public void testDelete() {
        repository.delete(stakeholder1);
        List<Stakeholder> all = repository.findAll();
        assertEquals(1, all.size());
        assertFalse(all.contains(stakeholder1));
    }
}
