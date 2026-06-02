package be.ucll.service;

import be.ucll.model.dto.CountersDto;
import be.ucll.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class StatsService {

    private final LocationRepository locationRepository;

    public StatsService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public CountersDto getCounters() {
        long totalBenches = locationRepository.countAllBenches();
        long distinctCountries = locationRepository.countDistinctCountries();

        Instant weekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
        long benchesLast7Days = locationRepository.countBenchesCreatedSince(weekAgo);

        return new CountersDto(
                totalBenches,
                distinctCountries,
                benchesLast7Days
        );
    }
}
