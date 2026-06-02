package be.ucll.model.dto;

import java.time.LocalDateTime;

public class MovementDTO {
    private LocalDateTime date;
    private String note;
    private Double latitude;
    private Double longitude;

    public MovementDTO() {}

    public MovementDTO(LocalDateTime date, String note, Double latitude, Double longitude) {
        this.date = date;
        this.note = note;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocalDateTime getDate() { return date; }
    public String getNote() { return note; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
}
