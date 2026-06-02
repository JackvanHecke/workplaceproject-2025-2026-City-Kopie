package be.ucll.service;

import be.ucll.model.CommunicationMessage;
import be.ucll.model.Profile;
import be.ucll.model.ProfileMessage;
import be.ucll.model.dto.CommunicationMessageCreateDTO;
import be.ucll.model.dto.CommunicationMessageDTO;
import be.ucll.model.dto.CommunicationRecipientDTO;
import be.ucll.model.enums.DeliveryChannel;
import be.ucll.model.enums.ProfileRole;
import be.ucll.repository.CommunicationMessageRepository;
import be.ucll.repository.ProfileMessageRepository;
import be.ucll.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommunicationMessageService {

    private final CommunicationMessageRepository messageRepository;
    private final ProfileRepository profileRepository;

    private final ProfileMessageRepository profileMessageRepository;

    public CommunicationMessageService(CommunicationMessageRepository messageRepository,
                                       ProfileRepository profileRepository,
                                       ProfileMessageRepository profileMessageRepository) {
        this.messageRepository = messageRepository;
        this.profileRepository = profileRepository;
        this.profileMessageRepository = profileMessageRepository;
    }




    public List<CommunicationMessageDTO> getAllMessages() {
        return messageRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CommunicationMessageDTO getMessageById(Long id) {
        CommunicationMessage msg = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Communication message not found with id: " + id));
        return toDTO(msg);
    }

    private void deliverToProfiles(CommunicationMessage msg) {
        if (msg.getChannels() == null) {
            return;
        }

        boolean inboxChannelPresent =
                msg.getChannels().contains(DeliveryChannel.PROFILE_MESSAGE)
                        || msg.getChannels().contains(DeliveryChannel.MOBILE_POPUP);

        if (!inboxChannelPresent) {
            return;
        }

        List<Profile> allProfiles = profileRepository.findAll();

        for (Profile profile : allProfiles) {
            if (Boolean.FALSE.equals(profile.getNotificationsEnabled())) {
                continue;
            }
            if (!matchesProfile(msg, profile)) {
                continue;
            }
            if (profileMessageRepository.existsByProfileIdAndMessageId(profile.getId(), msg.getId())) {
                continue;
            }

            ProfileMessage inboxItem = new ProfileMessage(profile, msg);
            inboxItem.setDeliveredAt(LocalDateTime.now());
            inboxItem.setRead(false);

            profileMessageRepository.save(inboxItem);
        }
    }


    @Transactional
    public CommunicationMessageDTO createMessage(CommunicationMessageCreateDTO dto) {
        Profile creator = profileRepository.findById(dto.getCreatedByProfileId())
                .orElseThrow(() -> new RuntimeException("Creator profile not found with id: " + dto.getCreatedByProfileId()));

        Set<Profile> explicitRecipients = resolveExplicitRecipients(dto.getExplicitProfileIds());

        CommunicationMessage msg = new CommunicationMessage();
        applyDtoToEntity(dto, msg, creator, explicitRecipients);

        CommunicationMessage saved = messageRepository.save(msg);

        // NEW: deliver to inbox for matching profiles
        deliverToProfiles(saved);

        return toDTO(saved);
    }


    @Transactional
    public CommunicationMessageDTO updateMessage(Long id, CommunicationMessageCreateDTO dto) {
        CommunicationMessage msg = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Communication message not found with id: " + id));

        Profile creator = msg.getCreatedBy();
        if (dto.getCreatedByProfileId() != null) {
            creator = profileRepository.findById(dto.getCreatedByProfileId())
                    .orElseThrow(() -> new RuntimeException("Creator profile not found with id: " + dto.getCreatedByProfileId()));
        }

        Set<Profile> explicitRecipients = resolveExplicitRecipients(dto.getExplicitProfileIds());

        applyDtoToEntity(dto, msg, creator, explicitRecipients);

        CommunicationMessage saved = messageRepository.save(msg);
        return toDTO(saved);
    }

    @Transactional
    public void deleteMessage(Long id) {
        CommunicationMessage msg = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Communication message not found with id: " + id));

        profileMessageRepository.deleteByMessageId(id);

        messageRepository.delete(msg);
    }


    /**
     * Get active messages relevant for a given profile:
     * - message is active and within time window
     * - profile matches role filter (if any)
     * - profile matches age filter (if any)
     * - if explicitRecipients is non-empty, profile must be in that set
     */
    public List<CommunicationMessageDTO> getActiveMessagesForProfile(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        LocalDateTime now = LocalDateTime.now();

        List<CommunicationMessage> activeMessages =
                messageRepository.findByActiveIsTrue();

        return activeMessages.stream()
                .filter(msg -> isWithinActiveWindow(msg, now))
                .filter(msg -> matchesProfile(msg, profile))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private boolean isWithinActiveWindow(CommunicationMessage msg, LocalDateTime now) {
        LocalDateTime start = msg.getStartsAt();
        LocalDateTime end = msg.getEndsAt();

        // no dates → rely solely on boolean “active”
        if (start == null && end == null) {
            return Boolean.TRUE.equals(msg.getActive());
        }

        if (start != null && now.isBefore(start)) {
            return false;
        }
        if (end != null && now.isAfter(end)) {
            return false;
        }

        return Boolean.TRUE.equals(msg.getActive());
    }


    // ===== Helper methods =====

    private Set<Profile> resolveExplicitRecipients(Set<Long> explicitProfileIds) {
        if (explicitProfileIds == null || explicitProfileIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(profileRepository.findAllById(explicitProfileIds));
    }

    private void applyDtoToEntity(CommunicationMessageCreateDTO dto,
                                  CommunicationMessage msg,
                                  Profile creator,
                                  Set<Profile> explicitRecipients) {

        if (dto.getTitle() != null) msg.setTitle(dto.getTitle());
        if (dto.getBody() != null) msg.setBody(dto.getBody());
        if (dto.getCategory() != null) msg.setCategory(dto.getCategory());
        if (dto.getChannels() != null) msg.setChannels(dto.getChannels());
        if (dto.getStartsAt() != null) msg.setStartsAt(dto.getStartsAt());
        msg.setEndsAt(dto.getEndsAt());

        if (dto.getActive() != null) {
            msg.setActive(dto.getActive());
        } else if (msg.getActive() == null) {
            msg.setActive(Boolean.TRUE);
        }

        msg.setMinAge(dto.getMinAge());
        msg.setMaxAge(dto.getMaxAge());

        if (dto.getTargetRoles() != null) {
            msg.setTargetRoles(dto.getTargetRoles());
        }

        msg.setCreatedBy(creator);
        if (msg.getCreatedAt() == null) {
            msg.setCreatedAt(LocalDateTime.now());
        }

        msg.setExplicitRecipients(explicitRecipients);
    }

    private boolean matchesProfile(CommunicationMessage msg, Profile profile) {
        ProfileRole role = profile.getRole();

        // role filter
        if (msg.getTargetRoles() != null && !msg.getTargetRoles().isEmpty()) {
            if (role == null || !msg.getTargetRoles().contains(role)) {
                return false;
            }
        }

        // age filter
        int age = profile.getAge();
        if (msg.getMinAge() != null && age < msg.getMinAge()) return false;
        if (msg.getMaxAge() != null && age > msg.getMaxAge()) return false;

        // explicit recipients: if non-empty, must be in set
        if (msg.getExplicitRecipients() != null && !msg.getExplicitRecipients().isEmpty()) {
            if (!msg.getExplicitRecipients().contains(profile)) {
                return false;
            }
        }

        return true;
    }

    private CommunicationMessageDTO toDTO(CommunicationMessage entity) {
        List<CommunicationRecipientDTO> recipients =
                entity.getExplicitRecipients() == null
                        ? Collections.emptyList()
                        : entity.getExplicitRecipients().stream()
                        .map(this::toRecipientDTO)
                        .collect(Collectors.toList());

        Profile creator = entity.getCreatedBy();

        return new CommunicationMessageDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                entity.getCategory(),
                entity.getChannels(),
                entity.getStartsAt(),
                entity.getEndsAt(),
                entity.getActive(),
                entity.getMinAge(),
                entity.getMaxAge(),
                entity.getTargetRoles(),
                creator != null ? creator.getId() : null,
                creator != null ? creator.getName() : null,
                entity.getCreatedAt(),
                recipients
        );
    }

    private CommunicationRecipientDTO toRecipientDTO(Profile profile) {
        return new CommunicationRecipientDTO(
                profile.getId(),
                profile.getName(),
                profile.getRole()
        );
    }
}
