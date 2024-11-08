package com.example.transpro.rest;

import com.example.transpro.model.Driver;
import com.example.transpro.service.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long id) {
        Optional<Driver> driver = driverService.findDriverById(id);
        return driver.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PostMapping("/add")
    public ResponseEntity<String> addDriver(@RequestBody Driver driver) {
        if (driverService.existsByLicenseNumber(driver.getLicenseNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Kierowca o takim numerze licencji lub numerze tachografu już istnieje");
        }
        if (driverService.existsByTachoCardNumber(driver.getTachoCardNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Kierowca o takim numerze licencji lub numerze tachografu już istnieje");
        }
        driverService.saveDriver(driver);
        return ResponseEntity.ok("Dane kierowcy zostały dodane");
    }



    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editDriver(@PathVariable Long id, @RequestBody Driver updatedDriver) {
        Optional<Driver> existingDriver = driverService.findDriverById(id);
        if (existingDriver.isPresent()) {
            Driver driver = existingDriver.get();
            driver.setFirstName(updatedDriver.getFirstName());
            driver.setLastName(updatedDriver.getLastName());
            driver.setLicenseNumber(updatedDriver.getLicenseNumber());
            driver.setLicenseExpiryDate(updatedDriver.getLicenseExpiryDate());
            driver.setTachoCardNumber(updatedDriver.getTachoCardNumber());
            driver.setTachoCardExpiryDate(updatedDriver.getTachoCardExpiryDate());
            if (driverService.existsByLicenseNumber(driver.getLicenseNumber())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Kierowca o takim numerze licencji lub numerze tachografu już istnieje");
            }
            if (driverService.existsByTachoCardNumber(driver.getTachoCardNumber())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Kierowca o takim numerze licencji lub numerze tachografu już istnieje");
            }
            driverService.saveDriver(driver);
            return ResponseEntity.ok("Dane kierowcy zostały zaktualizowane");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Driver with ID " + id + " not found");
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDriver(@PathVariable Long id) {
        if (driverService.findDriverById(id).isPresent()) {
            driverService.deleteDriver(id);
            return ResponseEntity.ok("Dane kierowcy zostały usunięte");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
