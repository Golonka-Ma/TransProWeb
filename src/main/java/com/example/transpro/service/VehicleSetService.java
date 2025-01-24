package com.example.transpro.service;

import com.example.transpro.model.Driver;
import com.example.transpro.model.Vehicle;
import com.example.transpro.model.VehicleSet;
import com.example.transpro.repository.DriverRepository;
import com.example.transpro.repository.VehicleRepository;
import com.example.transpro.repository.VehicleSetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleSetService {

    private final VehicleSetRepository vehicleSetRepository;
    private final VehicleRepository vehicleRepository;  // If needed
    private final DriverRepository driverRepository;    // If needed

    public VehicleSetService(VehicleSetRepository vehicleSetRepository,
                             VehicleRepository vehicleRepository,
                             DriverRepository driverRepository) {
        this.vehicleSetRepository = vehicleSetRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    // Create new set
    public VehicleSet createSet(Long truckId, Long trailerId, Long driverId) {
        Vehicle truck = vehicleRepository.findById(truckId)
                .orElseThrow(() -> new IllegalArgumentException("Truck not found"));
        Vehicle trailer = vehicleRepository.findById(trailerId)
                .orElseThrow(() -> new IllegalArgumentException("Trailer not found"));
        Driver driver = driverRepository.findById(driverId)
                .orElse(null);  // or throw if required

        VehicleSet vehicleSet = new VehicleSet();
        vehicleSet.setTruck(truck);
        vehicleSet.setTrailer(trailer);
        vehicleSet.setDriver(driver);
        // ... set more fields as needed
        return vehicleSetRepository.save(vehicleSet);
    }

    public VehicleSet getSet(Long id) {
        return vehicleSetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("VehicleSet not found"));
    }

    public VehicleSet updateSet(Long setId, Long truckId, Long trailerId, Long driverId) {
        VehicleSet existingSet = getSet(setId);

        if (truckId != null) {
            Vehicle newTruck = vehicleRepository.findById(truckId)
                    .orElseThrow(() -> new IllegalArgumentException("Truck not found"));
            existingSet.setTruck(newTruck);
        }
        if (trailerId != null) {
            Vehicle newTrailer = vehicleRepository.findById(trailerId)
                    .orElseThrow(() -> new IllegalArgumentException("Trailer not found"));
            existingSet.setTrailer(newTrailer);
        }
        if (driverId != null) {
            Driver newDriver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found"));
            existingSet.setDriver(newDriver);
        }

        return vehicleSetRepository.save(existingSet);
    }

    public void deleteSet(Long id) {
        vehicleSetRepository.deleteById(id);
    }

    public List<VehicleSet> getAllSets() {
        return vehicleSetRepository.findAll();
    }
}

