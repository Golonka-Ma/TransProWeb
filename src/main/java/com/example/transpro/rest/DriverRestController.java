package com.example.transpro.rest;

import com.example.transpro.model.Driver;
import com.example.transpro.service.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverRestController {

    private final DriverService driverService;

    @Autowired
    public DriverRestController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers() {
        List<Driver> drivers = driverService.findAllDrivers();
        return ResponseEntity.ok(drivers);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDriver(@RequestBody Driver driver) {
        driverService.saveDriver(driver);
        return ResponseEntity.ok("Driver added successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.ok("Driver deleted successfully");
    }
}
