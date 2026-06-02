package be.ucll.model.dto;

import java.util.Set;

import be.ucll.model.CompletedChecklistItem;

public class LocationDTO {

    private Long id;

    private String name;
    private String owner;

    private String street;
    private String houseNumber;
    private String zipCode;

    private String city;
    private String country;

    private String size;
    private String type;

    private Integer connectedRoutes;

    // keep as string (frontend splits to array)
    private String tags;

    // flags
    private Boolean showInApp;
    private Boolean mobile;
    private Boolean publicAvailable;

    // coords
    private Double latitude;
    private Double longitude;

    // comma-separated string (frontend splits to array)
    private String stations;

    // photo (relative path like /uploads/benches/x.jpg)
    private String photoUrl;

    // movement route info
    private Long movementRouteId;
    private String movementRouteName;
    private String movementRouteType;

    private Set<CompletedChecklistItem> completedChecklistItems;

    public LocationDTO() {
    }

    public LocationDTO(
            Long id,
            String name,
            String owner,
            String street,
            String houseNumber,
            String zipCode,
            String city,
            String country,
            String size,
            String type,
            Integer connectedRoutes,
            String tags,
            Boolean showInApp,
            Boolean mobile,
            Boolean publicAvailable,
            Double latitude,
            Double longitude,
            String stations,
            String photoUrl,
            Long movementRouteId,
            String movementRouteName,
            String movementRouteType,
            Set<CompletedChecklistItem> completedChecklistItems) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.size = size;
        this.type = type;
        this.connectedRoutes = connectedRoutes;
        this.tags = tags;
        this.showInApp = showInApp;
        this.mobile = mobile;
        this.publicAvailable = publicAvailable;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stations = stations;
        this.photoUrl = photoUrl;
        this.movementRouteId = movementRouteId;
        this.movementRouteName = movementRouteName;
        this.movementRouteType = movementRouteType;
        this.completedChecklistItems = completedChecklistItems;
    }

    public Set<CompletedChecklistItem> getCompletedChecklistItems() {
        return completedChecklistItems;
    }

    public void setCompletedChecklistItems(Set<CompletedChecklistItem> completedChecklistItems) {
        this.completedChecklistItems = completedChecklistItems;
    }

    // --- getters/setters (generate in IDE) ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getConnectedRoutes() {
        return connectedRoutes;
    }

    public void setConnectedRoutes(Integer connectedRoutes) {
        this.connectedRoutes = connectedRoutes;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean getShowInApp() {
        return showInApp;
    }

    public void setShowInApp(Boolean showInApp) {
        this.showInApp = showInApp;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public Boolean getPublicAvailable() {
        return publicAvailable;
    }

    public void setPublicAvailable(Boolean publicAvailable) {
        this.publicAvailable = publicAvailable;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStations() {
        return stations;
    }

    public void setStations(String stations) {
        this.stations = stations;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Long getMovementRouteId() {
        return movementRouteId;
    }

    public void setMovementRouteId(Long movementRouteId) {
        this.movementRouteId = movementRouteId;
    }

    public String getMovementRouteName() {
        return movementRouteName;
    }

    public void setMovementRouteName(String movementRouteName) {
        this.movementRouteName = movementRouteName;
    }

    public String getMovementRouteType() {
        return movementRouteType;
    }

    public void setMovementRouteType(String movementRouteType) {
        this.movementRouteType = movementRouteType;
    }
}
