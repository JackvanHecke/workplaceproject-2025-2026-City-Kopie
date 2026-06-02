// be/ucll/model/PartnershipCategory.java
package be.ucll.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PARTNERSHIP_CATEGORIES")
public class PartnershipCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // stable key so you can safely reference categories even if label changes
    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, length = 500)
    private String label;

    @Column(nullable = false)
    private Integer sortIndex;

    public PartnershipCategory() {}

    public PartnershipCategory(String code, String label, Integer sortIndex) {
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
