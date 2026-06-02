package be.ucll.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ucll.model.dto.CurrentUserDTO;
import be.ucll.model.dto.ProfileMessageDTO;
import be.ucll.model.dto.ProfileSummaryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import be.ucll.model.Profile;
import be.ucll.service.AuthTokenService;
import be.ucll.service.ProfileService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://localhost:8000")
public class ProfileRestController {

    private ProfileService profileService;
    private AuthTokenService authTokenService;

    public ProfileRestController(ProfileService profileService, AuthTokenService authTokenService) {
        this.profileService = profileService;
        this.authTokenService = authTokenService;
    }

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/simple")
    public List<ProfileSummaryDTO> getAllProfilesSimple() {
        return profileService.getAllProfiles()
                .stream()
                .map(p -> new ProfileSummaryDTO(
                        p.getId(),
                        p.getName(),
                        p.getEmail()))
                .toList();
    }

    @GetMapping("/token")
    public ResponseEntity<CurrentUserDTO> getProfileByToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // No header at all -> not logged in
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String tokenValue = authHeader.substring("Bearer ".length()).trim();
        if (tokenValue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var authToken = authTokenService.findByToken(tokenValue);
        if (authToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var profile = profileService.getProfileByToken(authToken);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CurrentUserDTO dto = new CurrentUserDTO(
                profile.getId(),
                profile.getName(),
                profile.getEmail(),
                profile.getNationality(),
                profile.getAge(),
                profile.getGender(),
                profile.getBmi(),
                profile.getMovementMinutes(),
                profile.getRegisteredAt(),
                profile.getRole());

        return ResponseEntity.ok(dto);
    }

    @PostMapping()
    public Profile addProfile(@Valid @RequestBody Profile profile) {
        return profileService.addProfile(profile);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(RuntimeException ex, WebRequest request) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("Error: ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * Returns the salt of the profile encoded in the URL (e.g.
     * http://localhost:8080/profile/salt?email=jack.vanhecke%40ucll.be)
     * 
     * @param email The email of the profile to get the salt of
     * @return The salt of the profile
     */
    @RequestMapping("/salt")
    public String getSalt(@RequestParam String email) {
        return profileService.getSalt(email);
    }

    public static class LoginRequest {
        public String email;
        public String hashedPassword;
    }

    /**
     * Returns the token of the profile
     * 
     * @param loginRequest The email and password of the profile
     * @return The token of the profile
     */
    @PostMapping(value = "/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return profileService.login(loginRequest.email, loginRequest.hashedPassword);
    }

    /**
     * Deletes all tokens for the profile (logs out the profile everywhere)
     * 
     * @param token The token of the profile
     */
    @RequestMapping("/logout")
    public void logout(@RequestHeader("Authorization") String token) {
        profileService.logout(token);
    }

    @GetMapping("/{id}/inbox")
    public List<ProfileMessageDTO> getInboxForProfile(
            @PathVariable Long id,
            @RequestParam(name = "limit", required = false) Integer limit) {
        return profileService.getInboxForProfile(id, limit);
    }

    @GetMapping("/{email}")
    public Profile getProfileByEmail(
            @PathVariable String email) {
        return profileService.getProfileByEmail(email);
    }

    @PutMapping("/{profileId}/inbox/{inboxId}/read")
    public ResponseEntity<Void> markInboxItemRead(
            @PathVariable Long profileId,
            @PathVariable Long inboxId) {
        profileService.markInboxItemRead(profileId, inboxId);
        return ResponseEntity.noContent().build();
    }

}
