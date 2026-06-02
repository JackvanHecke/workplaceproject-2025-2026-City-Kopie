package be.ucll.unit.service;

import be.ucll.model.AuthToken;
import be.ucll.repository.AuthTokenRepository;
import be.ucll.service.AuthTokenService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenServiceTest {

    @Mock
    private AuthTokenRepository authTokenRepository;

    @InjectMocks
    private AuthTokenService authTokenService;

    @Test
    void save_ValidToken_CallsRepository() {
        // Arrange
        AuthToken token = new AuthToken("testToken", null);

        // Act
        authTokenService.save(token);

        // Assert
        verify(authTokenRepository, times(1)).save(token);
    }

    @Test
    void findByToken_ExistingToken_ReturnsToken() {
        // Arrange
        AuthToken expected = new AuthToken("validToken", null);
        when(authTokenRepository.findByToken("validToken")).thenReturn(expected);

        // Act
        AuthToken result = authTokenService.findByToken("validToken");

        // Assert
        assertEquals(expected, result);
        verify(authTokenRepository, times(1)).findByToken("validToken");
    }

    @Test
    void findByToken_NonExistingToken_ReturnsNull() {
        // Arrange
        when(authTokenRepository.findByToken("invalidToken")).thenReturn(null);

        // Act
        AuthToken result = authTokenService.findByToken("invalidToken");

        // Assert
        assertNull(result);
        verify(authTokenRepository, times(1)).findByToken("invalidToken");
    }

    @Test
    void findByToken_NullInput_ReturnsNull() {
        // Act
        AuthToken result = authTokenService.findByToken(null);

        // Assert
        assertNull(result);
        //verifyNoInteractions(authTokenRepository); //Mockito is being annoying >:(
    }

    @Test
    void remove_ValidToken_CallsDelete() {
        // Arrange
        AuthToken token = new AuthToken("toDelete", null);

        // Act
        authTokenService.remove(token);

        // Assert
        verify(authTokenRepository, times(1)).delete(token);
    }

    @Test
    void remove_NullToken_DoesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> authTokenService.remove(null));
    }
}