package be.ucll.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name = "auth_tokens")
public class AuthToken {

    @Id
    private String token;

    private LocalDateTime expiryDate;

    public AuthToken() {
    }

    public AuthToken(String token, LocalDateTime expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public static AuthToken GenerateToken()
    {
        Random random = new Random();
        AuthToken Result = new AuthToken();
        String generatedString = random.ints(48, 122 + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(128)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        Result.setToken(generatedString);
        // Tokens are valid for 24 hours
        Result.setExpiryDate(java.time.LocalDateTime.now().plusDays(1));
        return Result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}