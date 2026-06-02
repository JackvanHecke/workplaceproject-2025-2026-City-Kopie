// be/ucll/controller/PartnershipCategoryRestController.java
package be.ucll.controller;

import be.ucll.model.dto.PartnershipCategoryDTO;
import be.ucll.service.PartnershipCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partnership-categories")
@CrossOrigin(origins = "http://localhost:8000")
public class PartnershipCategoryRestController {

    private final PartnershipCategoryService service;

    public PartnershipCategoryRestController(PartnershipCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<PartnershipCategoryDTO> getAll() {
        return service.getAll();
    }
}
