// src/main/java/be/ucll/model/dto/MovementRouteDTO.java
package be.ucll.model.dto;

public class MovementRouteDTO {

    private Long id;
    private String name;
    private String type;
    private String fileUrl; // optional, for imported route file

    public MovementRouteDTO() {}

    public MovementRouteDTO(Long id, String name, String type, String fileUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.fileUrl = fileUrl;
    }

    // Getters & setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
