// be/ucll/model/dto/PartnershipCategoryDTO.java
package be.ucll.model.dto;

public class PartnershipCategoryDTO {
    private Long id;
    private String code;
    private String label;
    private Integer sortIndex;

    public PartnershipCategoryDTO() {}

    public PartnershipCategoryDTO(Long id, String code, String label, Integer sortIndex) {
        this.id = id;
        this.code = code;
        this.label = label;
        this.sortIndex = sortIndex;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getLabel() { return label; }
    public Integer getSortIndex() { return sortIndex; }

    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setLabel(String label) { this.label = label; }
    public void setSortIndex(Integer sortIndex) { this.sortIndex = sortIndex; }
}
