// be/ucll/model/dto/LocationPartnershipDTO.java
package be.ucll.model.dto;

public class LocationPartnershipDTO {
    private Long categoryId;
    private boolean decided;

    public LocationPartnershipDTO() {}

    public LocationPartnershipDTO(Long categoryId, boolean decided) {
        this.categoryId = categoryId;
        this.decided = decided;
    }

    public Long getCategoryId() { return categoryId; }
    public boolean isDecided() { return decided; }

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setDecided(boolean decided) { this.decided = decided; }
}
