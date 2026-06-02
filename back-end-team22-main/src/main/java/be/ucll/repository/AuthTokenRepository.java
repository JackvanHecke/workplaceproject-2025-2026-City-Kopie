package be.ucll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import be.ucll.model.AuthToken;

public interface AuthTokenRepository extends JpaRepository<AuthToken, String> {
    public AuthToken findByToken(String token);
}
