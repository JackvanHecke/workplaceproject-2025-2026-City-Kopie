package be.ucll.model.dto;

public class CountersDto {

    private long totalBenches;
    private long distinctCountries;
    private long benchesLast7Days;

    public CountersDto(long totalBenches, long distinctCountries, long benchesLast7Days) {
        this.totalBenches = totalBenches;
        this.distinctCountries = distinctCountries;
        this.benchesLast7Days = benchesLast7Days;
    }

    public long getTotalBenches() {
        return totalBenches;
    }

    public long getDistinctCountries() {
        return distinctCountries;
    }

    public long getBenchesLast7Days() {
        return benchesLast7Days;
    }
}
