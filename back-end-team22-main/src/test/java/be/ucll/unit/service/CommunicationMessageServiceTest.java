package be.ucll.unit.service;

import be.ucll.model.CommunicationMessage;
import be.ucll.model.Profile;
import be.ucll.model.ProfileMessage;
import be.ucll.model.dto.CommunicationMessageCreateDTO;
import be.ucll.model.dto.CommunicationMessageDTO;
import be.ucll.model.enums.CommunicationCategory;
import be.ucll.model.enums.DeliveryChannel;
import be.ucll.model.enums.ProfileRole;
import be.ucll.repository.CommunicationMessageRepository;
import be.ucll.repository.ProfileMessageRepository;
import be.ucll.repository.ProfileRepository;
import be.ucll.service.CommunicationMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommunicationMessageServiceTest {

    @Mock
    private CommunicationMessageRepository messageRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileMessageRepository profileMessageRepository;

    @InjectMocks
    private CommunicationMessageService service;

    private Profile creator;
    private Profile recipient1;
    private Profile recipient2;

    @BeforeEach
    void setUp() {
        creator = new Profile(
                "Creator", 35, "creator@ucll.be", "M", "pw",
                "BE", new BigDecimal("23.0"),
                0, 0, 0, 0,
                LocalDateTime.now(), "saltC", "hashC"
        );
        creator.setId(1L);
        creator.setRole(ProfileRole.ADMIN);

        recipient1 = new Profile(
                "EndUser1", 25, "user1@ucll.be", "F", "pw",
                "BE", new BigDecimal("21.0"),
                0, 0, 0, 0,
                LocalDateTime.now(), "salt1", "hash1"
        );
        recipient1.setId(2L);
        recipient1.setRole(ProfileRole.END_USER);
        recipient1.setNotificationsEnabled(true);

        recipient2 = new Profile(
                "Coach1", 40, "coach1@ucll.be", "M", "pw",
                "BE", new BigDecimal("24.0"),
                0, 0, 0, 0,
                LocalDateTime.now(), "salt2", "hash2"
        );
        recipient2.setId(3L);
        recipient2.setRole(ProfileRole.COACH);
        recipient2.setNotificationsEnabled(true);
    }

    // ===== getAllMessages / getMessageById =====

    @Test
    void givenMessages_whenGetAllMessages_thenReturnDtos() {
        CommunicationMessage m1 = new CommunicationMessage();
        m1.setTitle("Title1");
        m1.setBody("Body1");
        m1.setCategory(CommunicationCategory.OTHER);
        m1.setChannels(Set.of(DeliveryChannel.APP_POPUP));
        m1.setStartsAt(LocalDateTime.now());
        m1.setCreatedBy(creator);

        CommunicationMessage m2 = new CommunicationMessage();
        m2.setTitle("Title2");
        m2.setBody("Body2");
        m2.setCategory(CommunicationCategory.ACTIVITY);
        m2.setChannels(Set.of(DeliveryChannel.PROFILE_MESSAGE));
        m2.setStartsAt(LocalDateTime.now());
        m2.setCreatedBy(creator);

        when(messageRepository.findAll()).thenReturn(List.of(m1, m2));

        List<CommunicationMessageDTO> result = service.getAllMessages();

        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0).getTitle());
        assertEquals("Title2", result.get(1).getTitle());
        verify(messageRepository).findAll();
    }

    @Test
    void givenExistingId_whenGetMessageById_thenReturnDto() {
        CommunicationMessage msg = new CommunicationMessage();
        msg.setTitle("Hello");
        msg.setBody("World");
        msg.setCategory(CommunicationCategory.OTHER);
        msg.setChannels(Set.of(DeliveryChannel.APP_POPUP));
        msg.setStartsAt(LocalDateTime.now());
        msg.setCreatedBy(creator);

        when(messageRepository.findById(10L)).thenReturn(Optional.of(msg));

        CommunicationMessageDTO dto = service.getMessageById(10L);

        assertEquals("Hello", dto.getTitle());
        assertEquals("World", dto.getBody());
        verify(messageRepository).findById(10L);
    }

    @Test
    void givenUnknownId_whenGetMessageById_thenThrow() {
        when(messageRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.getMessageById(99L)
        );

        assertTrue(ex.getMessage().contains("Communication message not found"));
    }

    // ===== createMessage (incl. deliverToProfiles path) =====

    @Test
    void givenCreateDto_whenCreateMessage_thenEntitySavedAndDeliveredToMatchingProfiles() {
        CommunicationMessageCreateDTO dto = new CommunicationMessageCreateDTO();
        dto.setTitle("New message");
        dto.setBody("Body content");
        dto.setCategory(CommunicationCategory.ACTIVITY);
        dto.setChannels(Set.of(DeliveryChannel.PROFILE_MESSAGE)); // -> triggers deliverToProfiles
        dto.setStartsAt(LocalDateTime.now().minusMinutes(5));
        dto.setEndsAt(LocalDateTime.now().plusHours(1));
        dto.setActive(true);
        dto.setMinAge(18);
        dto.setMaxAge(60);
        dto.setTargetRoles(Set.of(ProfileRole.END_USER));
        dto.setCreatedByProfileId(1L);

        // explicit recipients: only recipient1, but matchesProfile also allows via filters
        dto.setExplicitProfileIds(Set.of(2L));

        // resolve creator + explicit recipients
        when(profileRepository.findById(1L)).thenReturn(Optional.of(creator));
        when(profileRepository.findAllById(Set.of(2L))).thenReturn(List.of(recipient1));
        when(profileRepository.findAll()).thenReturn(List.of(recipient1, recipient2));

        // messageRepository.save should return an entity with id set
        CommunicationMessage saved = new CommunicationMessage();
        saved.setTitle(dto.getTitle());
        saved.setBody(dto.getBody());
        saved.setCategory(dto.getCategory());
        saved.setChannels(dto.getChannels());
        saved.setStartsAt(dto.getStartsAt());
        saved.setEndsAt(dto.getEndsAt());
        saved.setActive(dto.getActive());
        saved.setMinAge(dto.getMinAge());
        saved.setMaxAge(dto.getMaxAge());
        saved.setTargetRoles(dto.getTargetRoles());
        saved.setCreatedBy(creator);
        saved.setExplicitRecipients(new HashSet<>(List.of(recipient1)));

        // we can't call setId but we don't strictly need it in this test
        when(messageRepository.save(any(CommunicationMessage.class))).thenReturn(saved);

        // no existing ProfileMessage yet
        when(profileMessageRepository.existsByProfileIdAndMessageId(anyLong(), any()))
                .thenReturn(false);

        CommunicationMessageDTO result = service.createMessage(dto);

        assertEquals("New message", result.getTitle());
        assertEquals(CommunicationCategory.ACTIVITY, result.getCategory());
        assertEquals(dto.getChannels(), result.getChannels());

        // verify that we tried to deliver at least once
        ArgumentCaptor<ProfileMessage> pmCaptor = ArgumentCaptor.forClass(ProfileMessage.class);
        verify(profileMessageRepository, atLeastOnce()).save(pmCaptor.capture());

        List<ProfileMessage> savedInboxItems = pmCaptor.getAllValues();
        assertFalse(savedInboxItems.isEmpty());
        // All inbox items should reference the same saved message entity
        assertTrue(savedInboxItems.stream().allMatch(pm -> pm.getMessage() == saved));
    }

    @Test
    void givenCreateDtoWithoutInboxChannel_whenCreateMessage_thenNoInboxItemsCreated() {
        CommunicationMessageCreateDTO dto = new CommunicationMessageCreateDTO();
        dto.setTitle("New message");
        dto.setBody("Body content");
        dto.setCategory(CommunicationCategory.OTHER);
        dto.setChannels(Set.of(DeliveryChannel.APP_POPUP)); // no PROFILE_MESSAGE or MOBILE_POPUP
        dto.setStartsAt(LocalDateTime.now());
        dto.setActive(true);
        dto.setCreatedByProfileId(1L);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(creator));

        CommunicationMessage saved = new CommunicationMessage();
        saved.setTitle(dto.getTitle());
        saved.setBody(dto.getBody());
        saved.setCategory(dto.getCategory());
        saved.setChannels(dto.getChannels());
        saved.setStartsAt(dto.getStartsAt());
        saved.setActive(true);
        saved.setCreatedBy(creator);

        when(messageRepository.save(any(CommunicationMessage.class))).thenReturn(saved);

        CommunicationMessageDTO result = service.createMessage(dto);

        assertEquals("New message", result.getTitle());
        verify(profileMessageRepository, never()).save(any(ProfileMessage.class));
    }

    // ===== updateMessage =====

    @Test
    void givenExistingMessage_whenUpdateMessage_thenFieldsAreUpdated() {
        CommunicationMessage existing = new CommunicationMessage();
        existing.setTitle("Old");
        existing.setBody("OldBody");
        existing.setCategory(CommunicationCategory.OTHER);
        existing.setChannels(Set.of(DeliveryChannel.APP_POPUP));
        existing.setStartsAt(LocalDateTime.now().minusDays(1));
        existing.setActive(true);
        existing.setCreatedBy(creator);

        when(messageRepository.findById(10L)).thenReturn(Optional.of(existing));

        CommunicationMessageCreateDTO dto = new CommunicationMessageCreateDTO();
        dto.setTitle("Updated");
        dto.setBody("Updated body");
        dto.setCategory(CommunicationCategory.COURSE_SERIES);
        dto.setChannels(Set.of(DeliveryChannel.APP_POPUP, DeliveryChannel.PROFILE_MESSAGE));
        dto.setStartsAt(LocalDateTime.now());
        dto.setEndsAt(LocalDateTime.now().plusDays(1));
        dto.setActive(false);
        dto.setMinAge(20);
        dto.setMaxAge(40);
        dto.setTargetRoles(Set.of(ProfileRole.COACH));

        CommunicationMessage updated = new CommunicationMessage();
        updated.setTitle(dto.getTitle());
        updated.setBody(dto.getBody());
        updated.setCategory(dto.getCategory());
        updated.setChannels(dto.getChannels());
        updated.setStartsAt(dto.getStartsAt());
        updated.setEndsAt(dto.getEndsAt());
        updated.setActive(dto.getActive());
        updated.setMinAge(dto.getMinAge());
        updated.setMaxAge(dto.getMaxAge());
        updated.setTargetRoles(dto.getTargetRoles());
        updated.setCreatedBy(creator);

        when(messageRepository.save(existing)).thenReturn(updated);

        CommunicationMessageDTO result = service.updateMessage(10L, dto);

        assertEquals("Updated", result.getTitle());
        assertEquals("Updated body", result.getBody());
        assertEquals(CommunicationCategory.COURSE_SERIES, result.getCategory());
        assertEquals(dto.getChannels(), result.getChannels());
        assertEquals(dto.getMinAge(), result.getMinAge());
        assertEquals(dto.getMaxAge(), result.getMaxAge());
    }

    @Test
    void givenUnknownMessageId_whenUpdateMessage_thenThrow() {
        when(messageRepository.findById(99L)).thenReturn(Optional.empty());

        CommunicationMessageCreateDTO dto = new CommunicationMessageCreateDTO();
        dto.setTitle("Does not matter");

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.updateMessage(99L, dto)
        );

        assertTrue(ex.getMessage().contains("Communication message not found"));
        verify(messageRepository, never()).save(any());
    }

    // ===== deleteMessage =====

    @Test
    void givenExistingMessage_whenDeleteMessage_thenDeletesInboxAndMessage() {
        CommunicationMessage existing = new CommunicationMessage();
        existing.setTitle("To delete");

        when(messageRepository.findById(5L)).thenReturn(Optional.of(existing));

        service.deleteMessage(5L);

        verify(profileMessageRepository).deleteByMessageId(5L);
        verify(messageRepository).delete(existing);
    }

    @Test
    void givenUnknownMessageId_whenDeleteMessage_thenThrow() {
        when(messageRepository.findById(5L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.deleteMessage(5L)
        );

        assertTrue(ex.getMessage().contains("Communication message not found"));
        verify(profileMessageRepository, never()).deleteByMessageId(anyLong());
    }

    // ===== getActiveMessagesForProfile =====

    @Test
    void givenActiveMessages_whenGetActiveMessagesForProfile_thenFilterByWindowAndProfile() {
        Profile profile = new Profile();
        profile.setId(2L);
        profile.setAge(30);
        profile.setRole(ProfileRole.END_USER);

        when(profileRepository.findById(2L)).thenReturn(Optional.of(profile));

        LocalDateTime now = LocalDateTime.now();

        // Active, in window, matches role and age
        CommunicationMessage m1 = new CommunicationMessage();
        m1.setTitle("In window & match");
        m1.setBody("body1");
        m1.setCategory(CommunicationCategory.OTHER);
        m1.setChannels(Set.of(DeliveryChannel.APP_POPUP));
        m1.setActive(true);
        m1.setStartsAt(now.minusHours(1));
        m1.setEndsAt(now.plusHours(1));
        m1.setTargetRoles(Set.of(ProfileRole.END_USER));

        // Active but before start
        CommunicationMessage m2 = new CommunicationMessage();
        m2.setTitle("Too early");
        m2.setBody("body2");
        m2.setCategory(CommunicationCategory.OTHER);
        m2.setChannels(Set.of(DeliveryChannel.APP_POPUP));
        m2.setActive(true);
        m2.setStartsAt(now.plusHours(1));

        // Active but profile too young
        CommunicationMessage m3 = new CommunicationMessage();
        m3.setTitle("Age mismatch");
        m3.setBody("body3");
        m3.setCategory(CommunicationCategory.OTHER);
        m3.setChannels(Set.of(DeliveryChannel.APP_POPUP));
        m3.setActive(true);
        m3.setStartsAt(now.minusHours(1));
        m3.setEndsAt(now.plusHours(1));
        m3.setMinAge(40);

        when(messageRepository.findByActiveIsTrue())
                .thenReturn(List.of(m1, m2, m3));

        List<CommunicationMessageDTO> result =
                service.getActiveMessagesForProfile(2L);

        assertEquals(1, result.size());
        assertEquals("In window & match", result.get(0).getTitle());
    }
}
