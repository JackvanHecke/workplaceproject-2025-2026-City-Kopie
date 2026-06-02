package be.ucll.model.dto;

public class CoachLocationDTO {
    private Long id;
    private String benchName;
    private String benchCity;

    public CoachLocationDTO(Long id, String benchName, String benchCity) {
        this.id = id;
        this.benchName = benchName;
        this.benchCity = benchCity;
    }

    public Long getId() { return id; }
    public String getBenchName() { return benchName; }
    public String getBenchCity() { return benchCity; }
}
