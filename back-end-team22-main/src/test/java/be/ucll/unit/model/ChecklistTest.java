package be.ucll.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.model.*;

import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChecklistTest {

    private Checklist checklist;
    private Phase phase;
    private CompletedChecklistItem item1;
    private CompletedChecklistItem item2;

    @BeforeEach
    void setUp() {
        phase = mock(Phase.class);
        checklist = new Checklist( "Safety Checklist", "Important safety checks", phase);
        
        item1 = new CompletedChecklistItem();
        item2 = new CompletedChecklistItem();
    }

    @Test
    void testDefaultConstructor() {
        Checklist emptyChecklist = new Checklist();
        
        assertNull(emptyChecklist.getId());
        assertNull(emptyChecklist.getName());
        assertNull(emptyChecklist.getInfo());
        assertNull(emptyChecklist.getPhase());
        assertNotNull(emptyChecklist.getCompletionRecords());
        assertTrue(emptyChecklist.getCompletionRecords().isEmpty());
    }

    @Test
    void testParameterizedConstructorAndGetters() {
        checklist.setId(1L);
        assertEquals(1L, checklist.getId());
        assertEquals("Safety Checklist", checklist.getName());
        assertEquals("Important safety checks", checklist.getInfo());
        assertEquals(phase, checklist.getPhase());
        assertTrue(checklist.getCompletionRecords().isEmpty());
    }

    @Test
    void testSetters() {
        // Test id
        checklist.setId(2L);
        assertEquals(2L, checklist.getId());
        
        // Test name
        checklist.setName("Revised Checklist");
        assertEquals("Revised Checklist", checklist.getName());
        
        // Test info
        checklist.setInfo("Updated information");
        assertEquals("Updated information", checklist.getInfo());
    }

    @Test
    void testPhaseRelationship() {
        Phase newPhase = mock(Phase.class);
        checklist.setPhase(newPhase);
        assertEquals(newPhase, checklist.getPhase());
    }

    @Test
    void testAddCompletionRecord() {
        checklist.getCompletionRecords().add(item1);
        assertEquals(1, checklist.getCompletionRecords().size());
        assertTrue(checklist.getCompletionRecords().contains(item1));
    }

    @Test
    void testRemoveCompletionRecord() {
        checklist.getCompletionRecords().add(item1);
        checklist.getCompletionRecords().remove(item1);
        assertTrue(checklist.getCompletionRecords().isEmpty());
    }

    @Test
    void testSetCompletionRecords() {
        Set<CompletedChecklistItem> newSet = new HashSet<>();
        newSet.add(item1);
        newSet.add(item2);
        
        checklist.setCompletionRecords(newSet);
        assertEquals(2, checklist.getCompletionRecords().size());
        assertSame(newSet, checklist.getCompletionRecords());
    }

    @Test
    void testNullHandling() {
        checklist.setId(null);
        checklist.setName(null);
        checklist.setInfo(null);
        checklist.setPhase(null);
        checklist.setCompletionRecords(null);
        
        assertNull(checklist.getId());
        assertNull(checklist.getName());
        assertNull(checklist.getInfo());
        assertNull(checklist.getPhase());
        assertNull(checklist.getCompletionRecords());
    }

    @Test
    void testEdgeCaseStrings() {
        String longString = "A".repeat(500);
        
        checklist.setName(longString);
        checklist.setInfo(longString);
        
        assertEquals(longString, checklist.getName());
        assertEquals(longString, checklist.getInfo());
    }

    @Test
    void testBidirectionalRelationship() {
        CompletedChecklistItem item = new CompletedChecklistItem();
        checklist.getCompletionRecords().add(item);
        
        // Test backward relationship
        try {
            assertEquals(null, item.getChecklist());
        } catch(NullPointerException e) {
            fail("CompletedChecklistItem should maintain the backward relationship");
        }
        
        // Test item removal
        checklist.getCompletionRecords().remove(item);
        assertFalse(checklist.getCompletionRecords().contains(item));
    }
}