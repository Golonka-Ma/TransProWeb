package com.example.transpro.rest;

import com.example.transpro.model.Vehicle;
import com.example.transpro.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleRestController {

    private final VehicleService vehicleService;

    public VehicleRestController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // 1) Get all vehicles
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    // 2) Get single vehicle by ID
    @GetMapping("/{id}")
    public Vehicle getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicle(id);
    }

    // 3) Add vehicle (POST)
    @PostMapping("/add")
    public ResponseEntity<String> addVehicle(@RequestBody Vehicle vehicle) {
        Vehicle saved = vehicleService.addVehicle(vehicle);
        return ResponseEntity.ok("Pojazd został dodany. ID = " + saved.getId());
    }

    // 4) Edit vehicle (PUT)
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editVehicle(@PathVariable Long id, @RequestBody Vehicle updated) {
        vehicleService.updateVehicle(id, updated);
        return ResponseEntity.ok("Pojazd zaktualizowany pomyślnie.");
    }

    // 5) Delete vehicle (DELETE)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok("Pojazd usunięty pomyślnie.");
    }
}

