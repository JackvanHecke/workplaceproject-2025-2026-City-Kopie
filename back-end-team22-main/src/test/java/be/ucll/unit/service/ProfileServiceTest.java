package be.ucll.unit.service;

import be.ucll.model.AuthToken;
import be.ucll.model.CommunicationMessage;
import be.ucll.model.Profile;
import be.ucll.model.ProfileMessage;
import be.ucll.model.dto.ProfileMessageDTO;
import be.ucll.repository.ChecklistRepository;
import be.ucll.repository.ProfileMessageRepository;
import be.ucll.repository.ProfileRepository;
import be.ucll.service.AuthTokenService;
import be.ucll.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ChecklistRepository checklistRepository;

    @Mock
    private AuthTokenService authTokenService;

    @InjectMocks
    private ProfileService profileService;

    private Profile testProfile;
    private AuthToken validToken;
    private AuthToken expiredToken;

    @BeforeEach
    void setUp() {
        testProfile = new Profile();
        testProfile.setEmail("test@example.com");
        testProfile.setPasswordHash("hashedPass");
        testProfile.setSalt("saltValue");

        validToken = new AuthToken("validToken", LocalDateTime.now().plusHours(1));
        expiredToken = new AuthToken("expiredToken", LocalDateTime.now().minusHours(1));

        profile1 = new Profile(
                "Jack", 25, "jack@ucll.be", "M", "pw",
                "BE", new BigDecimal("20.2"),
                0, 0, 0, 0,
                LocalDateTime.now(), "salt1", "hash1"
        );
        profile1.setId(1L);

        profile2 = new Profile(
                "Wiebe", 30, "wiebe@ucll.be", "F", "pw2",
                "BE", new BigDecimal("22.5"),
                0, 0, 0, 0,
                LocalDateTime.now(), "salt2", "hash2"
        );
        profile2.setId(2L);
    }

    @Test
    void getAllProfiles_ReturnsAllProfiles() {
        // Arrange
        when(profileRepository.findAll()).thenReturn(Arrays.asList(testProfile, new Profile()));

        // Act
        List<Profile> result = profileService.getAllProfiles();

        // Assert
        assertEquals(2, result.size());
        verify(profileRepository, times(1)).findAll();
    }

    @Test
    void addProfile_NewEmail_SavesAndReturnsProfile() {
        // Arrange
        when(profileRepository.findFirstByEmail(testProfile.getEmail())).thenReturn(null);
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // Act
        Profile result = profileService.addProfile(testProfile);

        // Assert
        assertNotNull(result);
        assertEquals(testProfile.getEmail(), result.getEmail());
        verify(profileRepository, times(1)).save(testProfile);
    }

    @Test
    void addProfile_ExistingEmail_ThrowsException() {
        // Arrange
        when(profileRepository.findFirstByEmail(testProfile.getEmail())).thenReturn(testProfile);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> profileService.addProfile(testProfile));

        assertEquals("profile already exists.", exception.getMessage());
        verify(profileRepository, never()).save(any());
    }

    @Test
    void getProfileByEmail_Exists_ReturnsProfile() {
        // Arrange
        when(profileRepository.findFirstByEmail("test@example.com")).thenReturn(testProfile);

        // Act
        Profile result = profileService.getProfileByEmail("test@example.com");

        // Assert
        assertEquals(testProfile, result);
    }

    @Test
    void getProfileByEmail_NotExists_ReturnsNull() {
        // Arrange
        when(profileRepository.findFirstByEmail("nonexistent@example.com")).thenReturn(null);

        // Act
        Profile result = profileService.getProfileByEmail("nonexistent@example.com");

        // Assert
        assertNull(result);
    }

    @Test
    void addTokenToProfile_AddsTokenAndSaves() {
        // Arrange
        when(profileRepository.save(testProfile)).thenReturn(testProfile);

        // Act
        profileService.addTokenToProfile(testProfile, validToken);

        // Assert
        assertTrue(testProfile.getValidTokens().contains(validToken));
        verify(profileRepository, times(1)).save(testProfile);
    }

    @Test
    void getProfileByToken_ValidToken_ReturnsProfile() {
        // Arrange
        testProfile.getValidTokens().add(validToken);
        when(profileRepository.findAll()).thenReturn(Arrays.asList(testProfile, new Profile()));

        // Act
        Profile result = profileService.getProfileByToken(validToken);

        // Assert
        assertEquals(testProfile, result);
    }

    @Test
    void getProfileByToken_TokenNotFound_ReturnsNull() {
        // Arrange
        when(profileRepository.findAll()).thenReturn(List.of(testProfile));

        // Act
        Profile result = profileService.getProfileByToken(new AuthToken("invalid", LocalDateTime.now()));

        // Assert
        assertNull(result);
    }

    @Test
    void authenticateWithToken_NullToken_ReturnsNull() {
        assertNull(profileService.AuthenticateWithToken(null));
    }

    @Test
    void authenticateWithToken_InvalidToken_ReturnsNull() {
        // Arrange
        when(authTokenService.findByToken("invalidToken")).thenReturn(null);

        // Act
        Profile result = profileService.AuthenticateWithToken("invalidToken");

        // Assert
        assertNull(result);
    }

    @Test
    void authenticateWithToken_ExpiredToken_DeletesTokenAndReturnsNull() {
        // Arrange
        when(authTokenService.findByToken("expiredToken")).thenReturn(expiredToken);
        testProfile.getValidTokens().add(expiredToken);
        //when(profileRepository.findAll()).thenReturn(List.of(testProfile));
        
        // Act
        Profile result = profileService.AuthenticateWithToken("expiredToken");
        
        // Assert
        assertNull(result);
        verify(authTokenService, times(1)).remove(expiredToken);
    }

    @Test
    void authenticateWithToken_ValidToken_ReturnsProfile() {
        // Arrange
        when(authTokenService.findByToken("validToken")).thenReturn(validToken);
        testProfile.getValidTokens().add(validToken);
        when(profileRepository.findAll()).thenReturn(List.of(testProfile));
        
        // Act
        Profile result = profileService.AuthenticateWithToken("validToken");
        
        // Assert
        assertEquals(testProfile, result);
    }

    @Test
    void getSalt_ExistingProfile_ReturnsSalt() {
        // Arrange
        when(profileRepository.findFirstByEmail("test@example.com")).thenReturn(testProfile);
        
        // Act
        String result = profileService.getSalt("test@example.com");
        
        // Assert
        assertEquals("saltValue", result);
    }

    @Test
    void getSalt_NonExistingProfile_ReturnsErrorMessage() {
        // Arrange
        when(profileRepository.findFirstByEmail("unknown@test.com")).thenReturn(null);
        
        // Act
        String result = profileService.getSalt("unknown@test.com");
        
        // Assert
        assertEquals("Unknown Profile!", result);
    }


    @Test
    void login_ValidCredentials_ReturnsToken() {
        // Arrange
        when(profileRepository.findFirstByEmail("test@example.com")).thenReturn(testProfile);
        //when(authTokenService.save(any(AuthToken.class))).thenReturn(validToken);        
        // Act
        String result = profileService.login("test@example.com", "hashedPass");
        
        // Assert
        assertTrue(result.length() > 0);
        verify(authTokenService, times(1)).save(any(AuthToken.class));
        verify(profileRepository, times(1)).save(testProfile);
    }

    @Test
    void login_NonExistingProfile_ReturnsErrorMessage() {
        // Arrange
        when(profileRepository.findFirstByEmail("unknown@test.com")).thenReturn(null);
        
        // Act
        String result = profileService.login("unknown@test.com", "anyPassword");
        
        // Assert
        assertEquals("Unknown Profile!", result);
    }

    @Test
    void login_InvalidPassword_ReturnsErrorMessage() {
        // Arrange
        when(profileRepository.findFirstByEmail("test@example.com")).thenReturn(testProfile);
        
        // Act
        String result = profileService.login("test@example.com", "wrongPassword");
        
        // Assert
        assertEquals("Wrong Password!", result);
        verify(authTokenService, never()).save(any());
    }

    @Test
    void logout_ValidToken_RemovesAllTokens() {
        // Arrange
        testProfile.getValidTokens().add(validToken);
        testProfile.getValidTokens().add(expiredToken);
        when(authTokenService.findByToken("validToken")).thenReturn(validToken);
        when(profileRepository.findAll()).thenReturn(List.of(testProfile));
        
        // Act
        profileService.logout("validToken");
        
        // Assert
        verify(authTokenService, times(1)).remove(validToken);
        verify(authTokenService, times(1)).remove(expiredToken);
        assertFalse(testProfile.getValidTokens().isEmpty());
    }

    @Mock
    private ProfileMessageRepository profileMessageRepository;

    private Profile profile1;
    private Profile profile2;

    // === BASIC PROFILE CRUD ===

    @Test
    void givenProfiles_whenGetAllProfiles_thenReturnList() {
        when(profileRepository.findAll()).thenReturn(List.of(profile1, profile2));

        List<Profile> result = profileService.getAllProfiles();

        assertEquals(2, result.size());
        assertEquals("Jack", result.get(0).getName());
        verify(profileRepository).findAll();
    }

    @Test
    void givenNewEmail_whenAddProfile_thenSavedAndReturned() {
        when(profileRepository.findFirstByEmail("new@ucll.be")).thenReturn(null);

        Profile newProfile = new Profile(
                "New", 20, "new@ucll.be", "X", "pw",
                "BE", new BigDecimal("21.0"),
                0, 0, 0, 0,
                LocalDateTime.now(), "salt", "hash"
        );

        Profile result = profileService.addProfile(newProfile);

        assertEquals("New", result.getName());
        verify(profileRepository).save(newProfile);
    }

    @Test
    void givenExistingEmail_whenAddProfile_thenThrow() {
        when(profileRepository.findFirstByEmail("jack@ucll.be")).thenReturn(profile1);

        Profile duplicate = new Profile(
                "Dup", 25, "jack@ucll.be", "M", "pw",
                "BE", new BigDecimal("20.2"),
                0, 0, 0, 0,
                LocalDateTime.now(), "s", "h"
        );

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> profileService.addProfile(duplicate)
        );

        assertTrue(ex.getMessage().contains("profile already exists"));
        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    void givenEmail_whenGetProfileByEmail_thenDelegatesToRepository() {
        when(profileRepository.findFirstByEmail("jack@ucll.be")).thenReturn(profile1);

        Profile result = profileService.getProfileByEmail("jack@ucll.be");

        assertEquals(profile1, result);
        verify(profileRepository).findFirstByEmail("jack@ucll.be");
    }

    // === TOKENS & LOOKUPS ===

    @Test
    void givenProfileAndToken_whenAddTokenToProfile_thenTokenAddedAndProfileSaved() {
        AuthToken token = mock(AuthToken.class);

        profileService.addTokenToProfile(profile1, token);

        assertTrue(profile1.getValidTokens().contains(token));
        verify(profileRepository).save(profile1);
    }

    @Test
    void givenNullToken_whenGetProfileByToken_thenReturnNull() {
        assertNull(profileService.getProfileByToken(null));
    }

    @Test
    void givenTokenWithoutValue_whenGetProfileByToken_thenReturnNull() {
        AuthToken token = mock(AuthToken.class);
        when(token.getToken()).thenReturn(null);

        assertNull(profileService.getProfileByToken(token));
    }

    @Test
    void givenProfilesWithTokens_whenGetProfileByToken_thenReturnMatchingProfile() {
        AuthToken tokenInProfile = mock(AuthToken.class);
        when(tokenInProfile.getToken()).thenReturn("abc");

        profile1.getValidTokens().add(tokenInProfile);

        when(profileRepository.findAll()).thenReturn(List.of(profile1, profile2));

        AuthToken lookupToken = mock(AuthToken.class);
        when(lookupToken.getToken()).thenReturn("abc");

        Profile result = profileService.getProfileByToken(lookupToken);

        assertEquals(profile1, result);
    }

    @Test
    void givenNoMatchingToken_whenGetProfileByToken_thenReturnNull() {
        when(profileRepository.findAll()).thenReturn(List.of(profile1));

        AuthToken lookupToken = mock(AuthToken.class);
        when(lookupToken.getToken()).thenReturn("missing");

        assertNull(profileService.getProfileByToken(lookupToken));
    }

    // === AUTHENTICATE WITH TOKEN ===

    @Test
    void givenNullString_whenAuthenticateWithToken_thenReturnNull() {
        assertNull(profileService.AuthenticateWithToken(null));
    }

    @Test
    void givenUnknownToken_whenAuthenticateWithToken_thenReturnNull() {
        when(authTokenService.findByToken("nope")).thenReturn(null);

        assertNull(profileService.AuthenticateWithToken("nope"));
    }

    @Test
    void givenExpiredToken_whenAuthenticateWithToken_thenRemoveTokenAndReturnNull() {
        AuthToken token = mock(AuthToken.class);
        when(token.getExpiryDate()).thenReturn(LocalDateTime.now().minusMinutes(1));
        when(authTokenService.findByToken("expired")).thenReturn(token);

        Profile result = profileService.AuthenticateWithToken("expired");

        assertNull(result);
        verify(authTokenService).remove(token);
    }

    @Test
    void givenValidToken_whenAuthenticateWithToken_thenReturnProfile() {
        AuthToken tokenInProfile = mock(AuthToken.class);
        when(tokenInProfile.getToken()).thenReturn("secret");

        profile1.getValidTokens().add(tokenInProfile);
        when(profileRepository.findAll()).thenReturn(List.of(profile1));

        AuthToken lookupToken = mock(AuthToken.class);
        when(lookupToken.getToken()).thenReturn("secret");
        when(lookupToken.getExpiryDate()).thenReturn(LocalDateTime.now().plusMinutes(5));
        when(authTokenService.findByToken("secret")).thenReturn(lookupToken);

        Profile result = profileService.AuthenticateWithToken("secret");

        assertEquals(profile1, result);
    }

    // === SALT & LOGIN ===

    @Test
    void givenExistingProfile_whenGetSalt_thenReturnSalt() {
        when(profileRepository.findFirstByEmail("jack@ucll.be")).thenReturn(profile1);

        String salt = profileService.getSalt("jack@ucll.be");

        assertEquals("salt1", salt);
    }

    @Test
    void givenUnknownProfile_whenGetSalt_thenReturnMessage() {
        when(profileRepository.findFirstByEmail("x@ucll.be")).thenReturn(null);

        String salt = profileService.getSalt("x@ucll.be");

        assertEquals("Unknown Profile!", salt);
    }

    @Test
    void givenUnknownProfile_whenLogin_thenUnknownProfileMessage() {
        when(profileRepository.findFirstByEmail("x@ucll.be")).thenReturn(null);

        String result = profileService.login("x@ucll.be", "hash");

        assertEquals("Unknown Profile!", result);
    }

    @Test
    void givenWrongPassword_whenLogin_thenWrongPasswordMessage() {
        when(profileRepository.findFirstByEmail("jack@ucll.be")).thenReturn(profile1);

        String result = profileService.login("jack@ucll.be", "wronghash");

        assertEquals("Wrong Password!", result);
    }

    @Test
    void givenCorrectPassword_whenLogin_thenTokenReturnedAndSaved() {
        when(profileRepository.findFirstByEmail("jack@ucll.be")).thenReturn(profile1);

        // hash1 is the passwordHash we set in setUp()
        String result = profileService.login("jack@ucll.be", "hash1");

        assertNotNull(result);
        assertNotEquals("Wrong Password!", result);
        assertNotEquals("Unknown Profile!", result);

        // Token is created and saved
        verify(authTokenService, times(1)).save(any(AuthToken.class));
        // Profile is saved via addTokenToProfile
        verify(profileRepository, atLeastOnce()).save(eq(profile1));
    }

    // === LOGOUT ===

    @Test
    void givenUnknownToken_whenLogout_thenDoNothing() {
        when(authTokenService.findByToken("nope")).thenReturn(null);

        profileService.logout("nope");

        verify(authTokenService, never()).remove(any(AuthToken.class));
    }


    // === INBOX / MESSAGES ===

    @Test
    void givenProfileAndNoLimit_whenGetInboxForProfile_thenUseFindAllOrdered() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile1));

        ProfileMessage pm1 = mock(ProfileMessage.class);
        ProfileMessage pm2 = mock(ProfileMessage.class);

        CommunicationMessage cm1 = mock(CommunicationMessage.class);
        when(pm1.getId()).thenReturn(10L);
        when(pm1.getMessage()).thenReturn(cm1);
        when(cm1.getId()).thenReturn(100L);
        when(cm1.getTitle()).thenReturn("Msg1");
        when(cm1.getBody()).thenReturn("Body1");

        CommunicationMessage cm2 = mock(CommunicationMessage.class);
        when(pm2.getId()).thenReturn(11L);
        when(pm2.getMessage()).thenReturn(cm2);
        when(cm2.getId()).thenReturn(101L);
        when(cm2.getTitle()).thenReturn("Msg2");
        when(cm2.getBody()).thenReturn("Body2 which is a bit longer");

        when(pm1.getRead()).thenReturn(false);
        when(pm2.getRead()).thenReturn(true);

        when(profileMessageRepository.findByProfileIdOrderByDeliveredAtDesc(1L))
                .thenReturn(List.of(pm1, pm2));

        List<ProfileMessageDTO> dtos = profileService.getInboxForProfile(1L, null);

        assertEquals(2, dtos.size());
        verify(profileMessageRepository).findByProfileIdOrderByDeliveredAtDesc(1L);
        verify(profileMessageRepository, never()).findTop4ByProfileIdOrderByDeliveredAtDesc(anyLong());
    }

    @Test
    void givenProfileAndLimit_whenGetInboxForProfile_thenUseTop4Query() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile1));

        ProfileMessage pm1 = mock(ProfileMessage.class);
        CommunicationMessage cm1 = mock(CommunicationMessage.class);
        when(pm1.getId()).thenReturn(10L);
        when(pm1.getMessage()).thenReturn(cm1);
        when(cm1.getId()).thenReturn(100L);
        when(cm1.getTitle()).thenReturn("Msg1");
        when(cm1.getBody()).thenReturn("Body1");
        when(pm1.getRead()).thenReturn(false);

        when(profileMessageRepository.findTop4ByProfileIdOrderByDeliveredAtDesc(1L))
                .thenReturn(List.of(pm1));

        List<ProfileMessageDTO> dtos = profileService.getInboxForProfile(1L, 4);

        assertEquals(1, dtos.size());
        verify(profileMessageRepository).findTop4ByProfileIdOrderByDeliveredAtDesc(1L);
    }

    // === markInboxItemRead ===

    @Test
    void givenDifferentProfile_whenMarkInboxItemRead_thenThrow() {
        ProfileMessage pm = mock(ProfileMessage.class);
        Profile owner = new Profile();
        owner.setId(2L);
        when(pm.getProfile()).thenReturn(owner);

        when(profileMessageRepository.findById(50L)).thenReturn(Optional.of(pm));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> profileService.markInboxItemRead(1L, 50L)
        );

        assertTrue(ex.getMessage().contains("does not belong"));
        verify(profileMessageRepository, never()).save(any());
    }

    @Test
    void givenUnknownInboxItem_whenMarkInboxItemRead_thenThrow() {
        when(profileMessageRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> profileService.markInboxItemRead(1L, 99L)
        );

        assertTrue(ex.getMessage().contains("Inbox item not found"));
    }
}
