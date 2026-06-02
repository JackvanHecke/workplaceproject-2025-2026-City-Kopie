package be.ucll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.model.Location;
import be.ucll.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByLocation(Location location);
    List<Note> findByLocationOrderByUpdatedAtDesc(Location location);
}
