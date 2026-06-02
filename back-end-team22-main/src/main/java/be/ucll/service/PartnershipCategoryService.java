// be/ucll/service/PartnershipCategoryService.java
package be.ucll.service;

import be.ucll.model.PartnershipCategory;
import be.ucll.model.dto.PartnershipCategoryDTO;
import be.ucll.repository.PartnershipCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnershipCategoryService {

    private final PartnershipCategoryRepository repo;

    public PartnershipCategoryService(PartnershipCategoryRepository repo) {
        this.repo = repo;
    }

    public List<PartnershipCategoryDTO> getAll() {
        return repo.findAll().stream()
                .sorted((a, b) -> Integer.compare(a.getSortIndex(), b.getSortIndex()))
                .map(c -> new PartnershipCategoryDTO(c.getId(), c.getCode(), c.getLabel(), c.getSortIndex()))
                .toList();
    }
}
