package be.ucll.controller;

import be.ucll.model.Workshop;
import be.ucll.service.WorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workshops")
@CrossOrigin(origins = "http://localhost:8000")
public class WorkshopRestController {

    private WorkshopService workshopService;

    @Autowired
    public WorkshopRestController(WorkshopService workshopService) {
        this.workshopService = workshopService;
    }

    @GetMapping
    public List<Workshop> getWorkshops() {
        return workshopService.findWorkshops();
    }
}
