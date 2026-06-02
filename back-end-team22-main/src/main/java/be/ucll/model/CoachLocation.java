package be.ucll.model;

import jakarta.persistence.*;

@Entity
@Table(name = "COACH_LOCATIONS")
public class CoachLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @ManyToOne
    @JoinColumn(name = "bench_id", nullable = false)
    private Location location;

    public CoachLocation() {}

    public CoachLocation(Coach coach, Location location) {
        this.coach = coach;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
