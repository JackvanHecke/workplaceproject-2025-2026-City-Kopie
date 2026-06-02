// src/main/java/be/ucll/model/MovementRoute.java
package be.ucll.model;

import jakarta.persistence.*;

@Entity
@Table(name = "MOVEMENT_ROUTES")
public class MovementRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g. "Sluispark", "Parklus Leuven"
    @Column(nullable = false)
    private String name;

    // e.g. "WALKING", "RUNNING", "CYCLING" (for now just a free text string)
    @Column(nullable = false)
    private String type;

    // Optional – where the GPX/GeoJSON file is stored (can be extended later)
    @Column(name = "file_url")
    private String fileUrl;

    public MovementRoute() {}

    public MovementRoute(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public MovementRoute(Long id, String name, String type, String fileUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.fileUrl = fileUrl;
    }

    // ===== Getters & setters =====

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
