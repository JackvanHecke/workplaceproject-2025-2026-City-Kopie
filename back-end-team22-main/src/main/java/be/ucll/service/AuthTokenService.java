package be.ucll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.model.AuthToken;
import be.ucll.repository.AuthTokenRepository;

@Service
public class AuthTokenService {
    private AuthTokenRepository authTokenRepository;

    @Autowired
    public AuthTokenService(AuthTokenRepository authTokenRepository) {
        this.authTokenRepository = authTokenRepository;
    }

    public void save(AuthToken token) {
        authTokenRepository.save(token);
    }

    public AuthToken findByToken(String token) {
        return authTokenRepository.findByToken(token);
    }

    public void remove(AuthToken token)
    {
        authTokenRepository.delete(token);
    }
}
