package com.example.transpro.rest;

import com.example.transpro.model.VehicleSet;
import com.example.transpro.service.VehicleSetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle-sets")
public class VehicleSetRestController {

    private final VehicleSetService vehicleSetService;

    public VehicleSetRestController(VehicleSetService vehicleSetService) {
        this.vehicleSetService = vehicleSetService;
    }

    @PostMapping
    public ResponseEntity<?> createSet(@RequestParam Long truckId,
                                       @RequestParam Long trailerId,
                                       @RequestParam(required = false) Long driverId) {
        VehicleSet created = vehicleSetService.createSet(truckId, trailerId, driverId);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleSet> getSet(@PathVariable Long id) {
        VehicleSet set = vehicleSetService.getSet(id);
        return ResponseEntity.ok(set);
    }

    @GetMapping
    public ResponseEntity<List<VehicleSet>> getAllSets() {
        return ResponseEntity.ok(vehicleSetService.getAllSets());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSet(@PathVariable Long id,
                                       @RequestParam(required = false) Long truckId,
                                       @RequestParam(required = false) Long trailerId,
                                       @RequestParam(required = false) Long driverId) {
        VehicleSet updated = vehicleSetService.updateSet(id, truckId, trailerId, driverId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSet(@PathVariable Long id) {
        vehicleSetService.deleteSet(id);
        return ResponseEntity.ok("Set deleted successfully");
    }
}

