// src/main/java/be/ucll/controller/MovementRouteRestController.java
package be.ucll.controller;

import be.ucll.model.MovementRoute;
import be.ucll.model.dto.MovementRouteDTO;
import be.ucll.service.MovementRouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movement-routes")
@CrossOrigin(origins = "http://localhost:8000")
public class MovementRouteRestController {

    private final MovementRouteService movementRouteService;

    public MovementRouteRestController(MovementRouteService movementRouteService) {
        this.movementRouteService = movementRouteService;
    }

    @GetMapping
    public List<MovementRouteDTO> getAll() {
        return movementRouteService.getAll();
    }

    @GetMapping("/{id}")
    public MovementRouteDTO getById(@PathVariable Long id) {
        return movementRouteService.getById(id);
    }

    @PostMapping
    public ResponseEntity<MovementRouteDTO> create(@RequestBody MovementRouteDTO dto) {
        MovementRouteDTO created = movementRouteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovementRouteDTO> update(
            @PathVariable Long id,
            @RequestBody MovementRouteDTO dto
    ) {
        MovementRouteDTO updated = movementRouteService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * OPTIONAL: upload a route file (GPX/GeoJSON) and store the URL on the route.
     * Frontend can call: POST /movement-routes/{id}/file with form-data (file)
     */
    @PostMapping("/{id}/file")
    public ResponseEntity<MovementRouteDTO> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            MovementRouteDTO route = movementRouteService.getById(id);

            Path uploadDir = Paths.get("uploads/routes");
            Files.createDirectories(uploadDir);

            String filename = "route-" + id + "-" + file.getOriginalFilename();
            Path target = uploadDir.resolve(filename);

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            String url = "/uploads/routes/" + filename;
            route.setFileUrl(url);

            MovementRouteDTO updated = movementRouteService.update(id, route);
            return ResponseEntity.ok(updated);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
