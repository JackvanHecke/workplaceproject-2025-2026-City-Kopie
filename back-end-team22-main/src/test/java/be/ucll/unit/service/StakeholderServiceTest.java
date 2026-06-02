//package be.ucll.unit.service;
//
//import be.ucll.model.Stakeholder;
//import be.ucll.model.dto.StakeholderDTO;
//import be.ucll.repository.StakeholderRepository;
//import be.ucll.service.StakeholderService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class StakeholderServiceTest {
//
//    @InjectMocks
//    private StakeholderService service;
//
//    @Mock
//    private StakeholderRepository repository;
//
//    private Stakeholder stakeholder;
//
//    @BeforeEach
//    void setUp() {
//        stakeholder = new Stakeholder("Org A", "John Doe", "a@mail.com", "123", "Role");
//        stakeholder.setId(1L);
//    }
//
//    @Test
//    void createStakeholder_SavesAndReturns() {
//        when(repository.save(stakeholder)).thenReturn(stakeholder);
//
//        Stakeholder result = service.createStakeholder(stakeholder);
//
//        assertEquals(stakeholder, result);
//        verify(repository).save(stakeholder);
//    }
//
//    @Test
//    void findAll_ReturnsList() {
//        when(repository.findAll()).thenReturn(List.of(stakeholder));
//
//        List<Stakeholder> result = service.findAll();
//
//        assertEquals(1, result.size());
//        assertEquals(stakeholder, result.get(0));
//        verify(repository).findAll();
//    }
//
//    @Test
//    void deleteStakeholder_CallsRepository() {
//        doNothing().when(repository).deleteById(1L);
//
//        assertDoesNotThrow(() -> service.deleteStakeholder(1L));
//        verify(repository).deleteById(1L);
//    }
//
//    @Test
//    void updateStakeHolder_FullDto_UpdatesAllFields() {
//        StakeholderDTO dto = new StakeholderDTO();
//        dto.setOrganisation("New Org");
//        dto.setContactPerson("New Contact");
//        dto.setEmail("new@mail.com");
//        dto.setPhoneNumber("999");
//        dto.setRole("New Role");
//        dto.setPartnershipDecided(true);
//
//        when(repository.findById(1L)).thenReturn(Optional.of(stakeholder));
//        when(repository.save(stakeholder)).thenReturn(stakeholder);
//
//        Stakeholder updated = service.updateStakeHolder(1L, dto);
//
//        assertEquals("New Org", updated.getOrganisation());
//        assertEquals("New Contact", updated.getContactPerson());
//        assertEquals("new@mail.com", updated.getEmail());
//        assertEquals("999", updated.getPhoneNumber());
//        assertEquals("New Role", updated.getRole());
//        assertTrue(updated.isPartnershipDecided());
//
//        verify(repository).findById(1L);
//        verify(repository).save(stakeholder);
//    }
//
//    @Test
//    void updateStakeHolder_PartialDto_UpdatesNonNullOnly() {
//        StakeholderDTO dto = new StakeholderDTO();
//        dto.setOrganisation("Changed Org"); // Only update org
//
//        when(repository.findById(1L)).thenReturn(Optional.of(stakeholder));
//        when(repository.save(stakeholder)).thenReturn(stakeholder);
//
//        Stakeholder updated = service.updateStakeHolder(1L, dto);
//
//        assertEquals("Changed Org", updated.getOrganisation());
//        assertEquals("John Doe", updated.getContactPerson()); // unchanged
//    }
//
//    @Test
//    void updateStakeHolder_NonExisting_Throws() {
//        when(repository.findById(99L)).thenReturn(Optional.empty());
//
//        StakeholderDTO dto = new StakeholderDTO();
//        dto.setOrganisation("Does not matter");
//
//        RuntimeException ex = assertThrows(RuntimeException.class, () ->
//                service.updateStakeHolder(99L, dto)
//        );
//
//        assertTrue(ex.getMessage().contains("Stakeholder not found: 99"));
//        verify(repository, never()).save(any());
//    }
//}
