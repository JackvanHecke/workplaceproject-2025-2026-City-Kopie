package be.ucll.repository;

import be.ucll.model.ProfileFollow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileFollowRepository extends JpaRepository<ProfileFollow, Long> {

    List<ProfileFollow> findByProfile_Id(Long profileId);
}
