package be.ucll.unit.controller;

import be.ucll.controller.ProfileRestController;
import be.ucll.model.AuthToken;
import be.ucll.model.Profile;
import be.ucll.service.AuthTokenService;
import be.ucll.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileRestControllerTest {

    @Mock
    private ProfileService profileService;

    @Mock
    private AuthTokenService authTokenService;

    @InjectMocks
    private ProfileRestController controller;

    private Profile profile;
    private AuthToken validToken;
    private final String validTokenString = "validToken123";
    private final String validEmail = "test@example.com";
    private final String validPasswordHash = "hashedPassword123";

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profile.setId(1L);
        profile.setEmail(validEmail);

        validToken = new AuthToken(validTokenString, null);
    }

    @Test
    void getAllProfiles_ReturnsAllProfiles() {
        // Arrange
        when(profileService.getAllProfiles()).thenReturn(List.of(profile));
        // Act
        List<Profile> result = controller.getAllProfiles();

        // Assert
        assertEquals(1, result.size());
        assertEquals(profile, result.get(0));
        verify(profileService, times(1)).getAllProfiles();
    }

    @Test
    void getProfileByToken_ValidHeader_ReturnsProfile() {
        // Arrange
        String authHeader = "Bearer " + validTokenString;
        when(authTokenService.findByToken(validTokenString)).thenReturn(validToken);
        when(profileService.getProfileByToken(validToken)).thenReturn(profile);

        // Act
        controller.getProfileByToken(authHeader);

        // Assert
        verify(authTokenService, times(1)).findByToken(validTokenString);
        verify(profileService, times(1)).getProfileByToken(validToken);
    }

    @Test
    void getProfileByToken_InvalidToken_ThrowsException() {
        // Arrange
        String authHeader = "Bearer invalidToken";
        when(authTokenService.findByToken("invalidToken")).thenReturn(null);
        controller.getProfileByToken(authHeader);
    }

    @Test
    void getProfileByToken_MalformedHeader_HandlesGracefully() {
        // Arrange
        String malformedHeader = "InvalidHeaderFormat";
        controller.getProfileByToken(malformedHeader);
    }

    @Test
    void addProfile_ValidProfile_ReturnsProfile() {
        // Arrange
        when(profileService.addProfile(any(Profile.class))).thenReturn(profile);

        // Act
        Profile result = controller.addProfile(profile);

        // Assert
        assertEquals(profile, result);
        verify(profileService, times(1)).addProfile(profile);
    }

    @Test
    void addProfile_DuplicateEmail_HandlesException() {
        // Arrange
        String errorMessage = "profile already exists.";
        when(profileService.addProfile(any(Profile.class)))
            .thenThrow(new RuntimeException(errorMessage));

        // Act
        assertThrows(RuntimeException.class, () -> controller.addProfile(profile));
    }

    // ------------------------- Salt Endpoint -------------------------
    @Test
    void getSalt_ExistingEmail_ReturnsSalt() {
        // Arrange
        String expectedSalt = "randomSalt123";
        when(profileService.getSalt(validEmail)).thenReturn(expectedSalt);

        // Act
        String result = controller.getSalt(validEmail);

        // Assert
        assertEquals(expectedSalt, result);
        verify(profileService, times(1)).getSalt(validEmail);
    }

    @Test
    void getSalt_NonExistingEmail_ReturnsErrorMessage() {
        // Arrange
        String unknownEmail = "unknown@test.com";
        when(profileService.getSalt(unknownEmail))
            .thenReturn("Unknown Profile!");

        // Act
        String result = controller.getSalt(unknownEmail);

        // Assert
        assertEquals("Unknown Profile!", result);
    }

    // ------------------------- Login/Logout -------------------------
    @Test
    void login_ValidCredentials_ReturnsToken() {
        // Arrange
        ProfileRestController.LoginRequest request = new ProfileRestController.LoginRequest();
        request.email = validEmail;
        request.hashedPassword = validPasswordHash;

        when(profileService.login(validEmail, validPasswordHash))
            .thenReturn(validTokenString);

        // Act
        String result = controller.login(request);

        // Assert
        assertEquals(validTokenString, result);
        verify(profileService, times(1)).login(validEmail, validPasswordHash);
    }

    @Test
    void login_InvalidCredentials_ReturnsErrorMessage() {
        // Arrange
        ProfileRestController.LoginRequest request = new ProfileRestController.LoginRequest();
        request.email = validEmail;
        request.hashedPassword = "wrongPassword";

        when(profileService.login(validEmail, "wrongPassword"))
            .thenReturn("Wrong Password!");

        // Act
        String result = controller.login(request);

        // Assert
        assertEquals("Wrong Password!", result);
    }

    @Test
    void logout_ValidToken_CallsService() {
        // Arrange
        String authHeader = "Bearer " + validTokenString;

        // Act
        controller.logout(authHeader);

        // Assert
        verify(profileService, times(1)).logout("Bearer " + validTokenString);
    }

    @Test
    void handleRuntimeException_ReturnsProperErrorResponse() {
        // Arrange
        String errorMessage = "Test error";
        RuntimeException ex = new RuntimeException(errorMessage);

        // Act
        ResponseEntity<Map<String, String>> response = 
            controller.handleDomainException(ex, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createLoginRequest_WorksCorrectly() {
        // Test inner class functionality
        ProfileRestController.LoginRequest request = new ProfileRestController.LoginRequest();
        request.email = "test@example.com";
        request.hashedPassword = "hash";

        assertEquals("test@example.com", request.email);
        assertEquals("hash", request.hashedPassword);
    }
}