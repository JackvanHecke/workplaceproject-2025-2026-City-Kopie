package be.ucll.unit.model;

import org.junit.jupiter.api.Test;

import be.ucll.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;

class AuthTokenTest {

    private static final int TOKEN_LENGTH = 128;
    private static final Pattern VALID_TOKEN_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Duration EXPIRY_TOLERANCE = Duration.ofSeconds(1);

    @Test
    void testDefaultConstructor() {
        AuthToken token = new AuthToken();
        assertNull(token.getToken());
        assertNull(token.getExpiryDate());
    }

    @Test
    void testParameterizedConstructor() {
        String testToken = "test123";
        LocalDateTime testExpiry = LocalDateTime.now().plusDays(1);
        
        AuthToken token = new AuthToken(testToken, testExpiry);
        
        assertEquals(testToken, token.getToken());
        assertEquals(testExpiry, token.getExpiryDate());
    }

    @Test
    void testGettersAndSetters_Token() {
        String testToken = "abc987XYZ";
        AuthToken token = new AuthToken();
        
        token.setToken(testToken);
        assertEquals(testToken, token.getToken());
        
        token.setToken(null);
        assertNull(token.getToken());
    }

    @Test
    void testGettersAndSetters_ExpiryDate() {
        LocalDateTime testExpiry = LocalDateTime.now().plusHours(6);
        AuthToken token = new AuthToken();
        
        token.setExpiryDate(testExpiry);
        assertEquals(testExpiry, token.getExpiryDate());
        
        token.setExpiryDate(null);
        assertNull(token.getExpiryDate());
    }

    @Test
    void testGenerateToken_TokenLength() {
        AuthToken token = AuthToken.GenerateToken();
        assertEquals(TOKEN_LENGTH, token.getToken().length());
    }

    @Test
    void testGenerateToken_CharacterValidity() {
        AuthToken token = AuthToken.GenerateToken();
        assertTrue(VALID_TOKEN_PATTERN.matcher(token.getToken()).matches(),
            "Token contains invalid characters");
    }

    @Test
    void testGenerateToken_ExpiryDateTime() {
        LocalDateTime creationStart = LocalDateTime.now();
        AuthToken token = AuthToken.GenerateToken();
        LocalDateTime createdEnd = LocalDateTime.now();
        
        LocalDateTime expectedExpiryStart = creationStart.plusDays(1);
        LocalDateTime expectedExpiryEnd = createdEnd.plusDays(1).plus(EXPIRY_TOLERANCE);
        
        LocalDateTime actualExpiry = token.getExpiryDate();
        
        assertTrue(actualExpiry.isAfter(expectedExpiryStart.minus(EXPIRY_TOLERANCE)));
        assertTrue(actualExpiry.isBefore(expectedExpiryEnd.plus(EXPIRY_TOLERANCE)));
    }

    @Test
    void testGenerateToken_Uniqueness() {
        Set<String> tokens = new HashSet<>();
        int sampleSize = 50;
        
        for (int i = 0; i < sampleSize; i++) {
            AuthToken token = AuthToken.GenerateToken();
            assertTrue(tokens.add(token.getToken()), "Duplicate token generated");
        }
    }
}