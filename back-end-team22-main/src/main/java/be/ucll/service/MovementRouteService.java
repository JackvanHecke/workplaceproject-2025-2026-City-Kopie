// src/main/java/be/ucll/service/MovementRouteService.java
package be.ucll.service;

import be.ucll.model.MovementRoute;
import be.ucll.model.dto.MovementRouteDTO;
import be.ucll.repository.MovementRouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovementRouteService {

    private final MovementRouteRepository movementRouteRepository;

    public MovementRouteService(MovementRouteRepository movementRouteRepository) {
        this.movementRouteRepository = movementRouteRepository;
    }

    public List<MovementRouteDTO> getAll() {
        return movementRouteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MovementRouteDTO getById(Long id) {
        MovementRoute route = movementRouteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movement route not found with id: " + id));
        return toDTO(route);
    }

    @Transactional
    public MovementRouteDTO create(MovementRouteDTO dto) {
        MovementRoute entity = new MovementRoute();
        updateEntityFromDTO(entity, dto);
        MovementRoute saved = movementRouteRepository.save(entity);
        return toDTO(saved);
    }

    @Transactional
    public MovementRouteDTO update(Long id, MovementRouteDTO dto) {
        MovementRoute entity = movementRouteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movement route not found with id: " + id));

        updateEntityFromDTO(entity, dto);
        MovementRoute saved = movementRouteRepository.save(entity);
        return toDTO(saved);
    }

    // ===== Mapping helpers =====

    private MovementRouteDTO toDTO(MovementRoute route) {
        return new MovementRouteDTO(
                route.getId(),
                route.getName(),
                route.getType(),
                route.getFileUrl()
        );
    }

    private void updateEntityFromDTO(MovementRoute entity, MovementRouteDTO dto) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getType() != null) {
            entity.setType(dto.getType());
        }
        if (dto.getFileUrl() != null) {
            entity.setFileUrl(dto.getFileUrl());
        }
    }
}
