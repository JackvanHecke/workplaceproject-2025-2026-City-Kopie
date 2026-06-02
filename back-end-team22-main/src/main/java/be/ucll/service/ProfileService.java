package be.ucll.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import be.ucll.model.AuthToken;
import be.ucll.model.Profile;
import be.ucll.model.ProfileMessage;
import be.ucll.model.dto.ProfileMessageDTO;
import be.ucll.repository.PhaseRepository;
import be.ucll.repository.ProfileMessageRepository;      // only if you use it directly
import be.ucll.repository.ProfileRepository;
import jakarta.transaction.Transactional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    @SuppressWarnings("unused")
    private final PhaseRepository checklistRepository;
    private final AuthTokenService authTokenService;
    private final ProfileMessageRepository profileMessageRepository;

    // Single constructor = auto-wired by Spring (no @Autowired needed, but you can keep it if you like)
    public ProfileService(ProfileRepository profileRepository,
                          PhaseRepository checklistRepository,
                          AuthTokenService authTokenService,
                          ProfileMessageRepository profileMessageRepository) {

        this.profileRepository = profileRepository;
        this.checklistRepository = checklistRepository;
        this.authTokenService = authTokenService;
        this.profileMessageRepository = profileMessageRepository;
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Profile addProfile(Profile profileData) {
        Profile existingProfile = profileRepository.findFirstByEmail(profileData.getEmail());
        if (existingProfile != null) {
            throw new RuntimeException("profile already exists.");
        }
        profileRepository.save(profileData);
        return profileData;
    }

    public Profile getProfileByEmail(String email) {
        return profileRepository.findFirstByEmail(email);
    }

    /**
     * Adds the token to the profile and saves it
     */
    public void addTokenToProfile(Profile profile, AuthToken token) {
        profile.getValidTokens().add(token);
        profileRepository.save(profile);
    }

    /**
     * Returns the profile that has the given token.
     */
    public Profile getProfileByToken(AuthToken token) {
        if (token == null) return null;

        String tokenValue = token.getToken();
        if (tokenValue == null) return null;

        List<Profile> profiles = getAllProfiles();
        for (Profile profile : profiles) {
            if (profile.getValidTokens() == null) continue;

            for (AuthToken t : profile.getValidTokens()) {
                if (tokenValue.equals(t.getToken())) {
                    return profile;
                }
            }
        }
        return null;
    }

    /**
     * Authenticates the profile with the token
     */
    public Profile AuthenticateWithToken(String token) {
        if (token == null) {
            return null;
        }

        AuthToken authToken = authTokenService.findByToken(token);
        if (authToken == null) {
            return null;
        }

        if (authToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            // Token is expired, delete it
            authTokenService.remove(authToken);
            return null;
        }

        return getProfileByToken(authToken);
    }

    public String getSalt(String email) {
        Profile profile = getProfileByEmail(email);
        if (profile == null) {
            return "Unknown Profile!";
        }
        return profile.getSalt();
    }

    public String login(String email, String hashedPassword) {
        Profile profile = getProfileByEmail(email);
        if (profile == null) {
            return "Unknown Profile!";
        }

        if (profile.getPasswordHash().equals(hashedPassword)) {
            // Create a token and add it to the profile
            AuthToken authToken = AuthToken.GenerateToken();
            authTokenService.save(authToken);
            addTokenToProfile(profile, authToken);

            // return the token string
            return authToken.getToken();
        }

        return "Wrong Password!";
    }

    public void logout(String token) {
        AuthToken authToken = authTokenService.findByToken(token);
        if (authToken == null) {
            return;
        }

        Profile profile = getProfileByToken(authToken);
        if (profile == null) {
            return;
        }

        // Delete all tokens for that Profile
        Set<AuthToken> profileTokens = profile.getValidTokens();
        for (AuthToken profileToken : profileTokens) {
            authTokenService.remove(profileToken);
        }
    }

    public List<ProfileMessageDTO> getInboxForProfile(Long profileId, Integer limit) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        List<ProfileMessage> items;

        if (limit != null && limit > 0) {
            items = profileMessageRepository.findTop4ByProfileIdOrderByDeliveredAtDesc(profile.getId());
        } else {
            items = profileMessageRepository.findByProfileIdOrderByDeliveredAtDesc(profile.getId());
        }

        return items.stream()
                .map(this::toProfileMessageDTO)
                .toList();
    }

    private ProfileMessageDTO toProfileMessageDTO(ProfileMessage pm) {
        String body = pm.getMessage().getBody();
        String preview = body != null && body.length() > 80
                ? body.substring(0, 77) + "..."
                : body;

        return new ProfileMessageDTO(
                pm.getId(),
                pm.getMessage().getId(),
                pm.getMessage().getTitle(),
                preview,
                pm.getRead(),
                pm.getDeliveredAt(),
                pm.getMessage().getChannels()
        );
    }



    @Transactional
    public void markInboxItemRead(Long profileId, Long inboxItemId) {
        ProfileMessage pm = profileMessageRepository.findById(inboxItemId)
                .orElseThrow(() -> new RuntimeException("Inbox item not found with id: " + inboxItemId));

        if (!pm.getProfile().getId().equals(profileId)) {
            throw new RuntimeException("Inbox item does not belong to this profile");
        }

        if (!Boolean.TRUE.equals(pm.getRead())) {
            pm.setRead(true);
            pm.setReadAt(LocalDateTime.now());
            profileMessageRepository.save(pm);
        }
    }
}
