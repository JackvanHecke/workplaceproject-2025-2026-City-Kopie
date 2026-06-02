package be.ucll.service;

import be.ucll.model.Location;
import be.ucll.model.Profile;
import be.ucll.model.ProfileFollow;
import be.ucll.model.dto.CommunicationMessageCreateDTO;
import be.ucll.model.enums.DeliveryChannel;
import be.ucll.repository.LocationRepository;
import be.ucll.repository.ProfileFollowRepository;
import be.ucll.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileFollowService {

    private final ProfileRepository profileRepository;
    private final LocationRepository locationRepository;
    private final ProfileFollowRepository followRepository;
    private final LocationService locationService;
    private final CommunicationMessageService communicationMessageService;

    private final Long systemProfileId;

    public ProfileFollowService(ProfileRepository profileRepository,
                                LocationRepository locationRepository,
                                ProfileFollowRepository followRepository,
                                LocationService locationService,
                                CommunicationMessageService communicationMessageService,
                                @Value("${app.notifications.systemProfileId:1}") Long systemProfileId) {
        this.profileRepository = profileRepository;
        this.locationRepository = locationRepository;
        this.followRepository = followRepository;
        this.locationService = locationService;
        this.communicationMessageService = communicationMessageService;
        this.systemProfileId = systemProfileId;
    }

    @Transactional
    public ProfileFollow followLocation(Long profileId, Long locationId, int radiusKm) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown profile " + profileId));
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown location " + locationId));

        // lastChecked init op "nu" zodat je niet meteen alles sinds epoch krijgt
        ProfileFollow follow = new ProfileFollow(profile, location, radiusKm, Instant.now());
        return followRepository.save(follow);
    }

    @Transactional(readOnly = true)
    public List<ProfileFollow> getFollowsForProfile(Long profileId) {
        return followRepository.findByProfile_Id(profileId);
    }

    /**
     * Check voor nieuwe benches binnen de radius sinds lastChecked.
     * - update lastChecked
     * - als er nieuwe zijn: maak een inbox melding (belletje) voor dit profiel
     */
    @Transactional
    public List<LocationService.NearbyLocation> checkNewBenchesForFollow(Long followId) {
        ProfileFollow follow = followRepository.findById(followId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown follow " + followId));

        Location center = follow.getLocation();

        // veiligheid: als center geen coords heeft kan je geen radius check doen
        if (center.getLatitude() == null || center.getLongitude() == null) {
            follow.setLastChecked(Instant.now());
            followRepository.save(follow);
            return List.of();
        }

        Instant since = (follow.getLastChecked() != null)
                ? follow.getLastChecked()
                : Instant.EPOCH;

        List<LocationService.NearbyLocation> items = locationService.findNewLocationsWithin(
                center.getLatitude(),
                center.getLongitude(),
                follow.getRadiusKm(),
                since
        );

        // update lastChecked time (zodat je niet blijft spammen)
        follow.setLastChecked(Instant.now());
        followRepository.save(follow);

        // Maak een notification (inbox) als er nieuwe benches zijn
        if (!items.isEmpty()) {
            createNotificationForFollow(follow, items);
        }

        return items;
    }

    private void createNotificationForFollow(ProfileFollow follow, List<LocationService.NearbyLocation> items) {
        int shown = Math.min(items.size(), 5);

        String details = items.stream()
                .limit(shown)
                .map(n -> formatBenchLine(n))
                .collect(Collectors.joining("\n"));

        String extra = (items.size() > shown)
                ? "\n… en nog " + (items.size() - shown) + " meer."
                : "";

        CommunicationMessageCreateDTO dto = new CommunicationMessageCreateDTO();
        dto.setTitle("Nieuwe beweegtoestellen in je buurt");
        dto.setBody(
                "Er zijn " + items.size() + " nieuwe toestellen binnen " + follow.getRadiusKm() + " km:\n\n"
                        + details
                        + extra
        );

        dto.setActive(true);


        dto.setChannels(Set.of(DeliveryChannel.PROFILE_MESSAGE));

        dto.setCreatedByProfileId(systemProfileId);

        dto.setExplicitProfileIds(Set.of(follow.getProfile().getId()));

        communicationMessageService.createMessage(dto);
    }

    private String formatBenchLine(LocationService.NearbyLocation n) {
        Location b = n.getLocation();

        // PAS AAN als jouw entity andere getters gebruikt:
        Long id = b.getBenchId();                // of b.getId()
        String name = b.getBenchName();          // of b.getName()
        String city = b.getBenchCity();          // of b.getCity()
        String street = b.getBenchStreet();      // of b.getStreet()
        String nr = b.getBenchHouseNumber();     // of b.getHouseNumber()
        String country = b.getBenchCountry();    // optioneel

        String addr = joinAddress(street, nr, city, country);

        String safeName = (name != null && !name.isBlank()) ? name : "(zonder naam)";
        String safeAddr = (!addr.isBlank()) ? addr : "(locatie onbekend)";

        return "• #" + id + " " + safeName + " — " + safeAddr
                + String.format(" (%.1f km)", n.getDistanceKm());
    }

    private String joinAddress(String street, String nr, String city, String country) {
        String s = (street != null) ? street.trim() : "";
        String n = (nr != null) ? nr.trim() : "";
        String c = (city != null) ? city.trim() : "";
        String co = (country != null) ? country.trim() : "";

        StringBuilder out = new StringBuilder();
        if (!s.isBlank()) out.append(s);
        if (!n.isBlank()) {
            if (out.length() > 0) out.append(" ");
            out.append(n);
        }
        if (!c.isBlank()) {
            if (out.length() > 0) out.append(", ");
            out.append(c);
        }
        if (!co.isBlank()) {
            if (out.length() > 0) out.append(", ");
            out.append(co);
        }
        return out.toString();
    }
}
