package be.ucll.controller;

import be.ucll.model.dto.CountersDto;
import be.ucll.service.StatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class StatsRestController {

    private final StatsService statsService;

    public StatsRestController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/counters")
    public CountersDto getCounters() {
        return statsService.getCounters();
    }
}
