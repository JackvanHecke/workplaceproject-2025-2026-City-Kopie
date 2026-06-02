package be.ucll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import be.ucll.model.Profile;

import java.util.Optional;


public interface ProfileRepository  extends JpaRepository<Profile, Long> {
    Profile findFirstByEmail(String email);
}
