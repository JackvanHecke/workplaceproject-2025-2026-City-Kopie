//package be.ucll.unit.controller;
//
//import be.ucll.controller.StakeholderRestController;
//import be.ucll.model.Stakeholder;
//import be.ucll.model.dto.StakeholderDTO;
//import be.ucll.service.StakeholderService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class StakeholderRestControllerTest {
//
//    @Mock
//    private StakeholderService stakeholderService;
//
//    @InjectMocks
//    private StakeholderRestController controller;
//
//    private Stakeholder stakeholder;
//    private StakeholderDTO dto;
//    private final Long stakeholderId = 1L;
//
//    @BeforeEach
//    void setUp() {
//        stakeholder = new Stakeholder();
//        stakeholder.setId(stakeholderId);
//        stakeholder.setOrganisation("City Org");
//        stakeholder.setContactPerson("John Doe");
//
//        dto = new StakeholderDTO();
//        dto.setOrganisation("Updated Org");
//        dto.setContactPerson("Updated Contact");
//    }
//
//    @Test
//    void createStakeholder_ValidInput_ReturnsCreatedStakeholder() {
//        when(stakeholderService.createStakeholder(any(Stakeholder.class))).thenReturn(stakeholder);
//
//        Stakeholder result = controller.createStakeholder(stakeholder);
//
//        assertNotNull(result);
//        assertEquals(stakeholder.getId(), result.getId());
//        verify(stakeholderService).createStakeholder(stakeholder);
//    }
//
//    @Test
//    void getAllStakeholders_ReturnsAllStakeholders() {
//        when(stakeholderService.findAll()).thenReturn(List.of(stakeholder));
//
//        List<Stakeholder> result = controller.getAllStakeholders();
//
//        assertEquals(1, result.size());
//        assertEquals(stakeholder, result.get(0));
//        verify(stakeholderService).findAll();
//    }
//
//    @Test
//    void getAllStakeholders_EmptyList_ReturnsEmptyList() {
//        when(stakeholderService.findAll()).thenReturn(List.of());
//
//        List<Stakeholder> result = controller.getAllStakeholders();
//
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void updateStakeholder_ValidId_ReturnsUpdatedStakeholder() {
//        Stakeholder updated = new Stakeholder();
//        updated.setOrganisation("Updated Org");
//
//        when(stakeholderService.updateStakeHolder(stakeholderId, dto)).thenReturn(updated);
//
//        Stakeholder result = controller.updateStakeholder(stakeholderId, dto);
//
//        assertEquals("Updated Org", result.getOrganisation());
//        verify(stakeholderService).updateStakeHolder(stakeholderId, dto);
//    }
//
//    @Test
//    void updateStakeholder_NonExistingId_ThrowsException() {
//        Long nonExistingId = 99L;
//        when(stakeholderService.updateStakeHolder(nonExistingId, dto))
//                .thenThrow(new RuntimeException("Stakeholder not found"));
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> controller.updateStakeholder(nonExistingId, dto));
//
//        assertEquals("Stakeholder not found", exception.getMessage());
//    }
//
//    @Test
//    void deleteStakeholder_ExistingId_DeletesSuccessfully() {
//        doNothing().when(stakeholderService).deleteStakeholder(stakeholderId);
//
//        assertDoesNotThrow(() -> controller.deleteStakeholder(stakeholderId));
//        verify(stakeholderService).deleteStakeholder(stakeholderId);
//    }
//
//    @Test
//    void deleteStakeholder_NonExistingId_ThrowsException() {
//        Long nonExistingId = 99L;
//        doThrow(new RuntimeException("Stakeholder not found"))
//                .when(stakeholderService).deleteStakeholder(nonExistingId);
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> controller.deleteStakeholder(nonExistingId));
//
//        assertEquals("Stakeholder not found", exception.getMessage());
//    }
//
//    @Test
//    void createStakeholder_NullInput_ThrowsException() {
//        assertThrows(RuntimeException.class, () -> controller.createStakeholder(null));
//    }
//
//    @Test
//    void updateStakeholder_NullDTO_ThrowsException() {
//        assertThrows(RuntimeException.class, () -> controller.updateStakeholder(stakeholderId, null));
//    }
//
//    @Test
//    void concurrentUpdateRequests_HandlesCorrectly() throws Exception {
//        when(stakeholderService.updateStakeHolder(stakeholderId, dto)).thenReturn(stakeholder);
//
//        int numberOfThreads = 2;
//        CountDownLatch latch = new CountDownLatch(numberOfThreads);
//
//        Runnable updateTask = () -> {
//            try {
//                controller.updateStakeholder(stakeholderId, dto);
//            } finally {
//                latch.countDown();
//            }
//        };
//
//        new Thread(updateTask).start();
//        new Thread(updateTask).start();
//
//        boolean allThreadsCompleted = latch.await(2, TimeUnit.SECONDS);
//        assertTrue(allThreadsCompleted, "Not all threads completed within timeout");
//
//        verify(stakeholderService, times(numberOfThreads))
//                .updateStakeHolder(stakeholderId, dto);
//    }
//}
