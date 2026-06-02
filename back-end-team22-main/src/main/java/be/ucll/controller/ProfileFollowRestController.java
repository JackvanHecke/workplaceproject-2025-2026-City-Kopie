package be.ucll.controller;

import be.ucll.model.ProfileFollow;
import be.ucll.service.ProfileFollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/profiles/{profileId}/follows")
@CrossOrigin(origins = "*")
public class ProfileFollowRestController {

    private final ProfileFollowService followService;

    public ProfileFollowRestController(ProfileFollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    public ResponseEntity<?> createFollow(@PathVariable Long profileId,
                                          @RequestBody Map<String, Object> payload) {
        Long locationId = ((Number) payload.get("locationId")).longValue();
        int radiusKm = ((Number) payload.getOrDefault("radiusKm", 10)).intValue();

        ProfileFollow follow = followService.followLocation(profileId, locationId, radiusKm);

        return ResponseEntity.ok(Map.of(
                "followId", follow.getId(),
                "profileId", profileId,
                "locationId", locationId,
                "radiusKm", radiusKm
        ));
    }

    @GetMapping
    public ResponseEntity<?> listFollows(@PathVariable Long profileId) {
        return ResponseEntity.ok(followService.getFollowsForProfile(profileId));
    }

    @GetMapping("/{followId}/check")
    public ResponseEntity<?> checkNewForFollow(@PathVariable Long profileId,
                                               @PathVariable Long followId) {

        var items = followService.checkNewBenchesForFollow(followId);
        return ResponseEntity.ok(Map.of("items", items));
    }
}
