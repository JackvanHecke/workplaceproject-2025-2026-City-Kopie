package be.ucll.controller;

import be.ucll.model.Stakeholder;
import be.ucll.model.dto.StakeholderDTO;
import be.ucll.service.StakeholderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stakeholders")
@CrossOrigin(origins = "http://localhost:8000")
public class StakeholderRestController {

    private StakeholderService stakeholderService;

    @Autowired
    public StakeholderRestController(StakeholderService stakeholderService) {
        this.stakeholderService = stakeholderService;
    }

    @PostMapping
    public Stakeholder createStakeholder(@RequestBody StakeholderDTO dto) {
        return stakeholderService.createStakeholder(dto);
    }

    @GetMapping
    public List<Stakeholder> getAllStakeholders() {
        return stakeholderService.findAll();
    }

    @PutMapping("/{id}")
    public Stakeholder updateStakeholder(@PathVariable Long id,
                                         @RequestBody StakeholderDTO dto) {
        return stakeholderService.updateStakeHolder(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteStakeholder(@PathVariable Long id) {
        stakeholderService.deleteStakeholder(id);
    }
}
