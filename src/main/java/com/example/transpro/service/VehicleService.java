package com.example.transpro.service;


import com.example.transpro.model.Vehicle;
import com.example.transpro.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicle(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with ID: " + id));
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        // Validate uniqueness of registrationNumber, VIN, etc. if needed
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
        Vehicle existing = getVehicle(id);
        // Update fields
        existing.setRegistrationNumber(updatedVehicle.getRegistrationNumber());
        existing.setBrand(updatedVehicle.getBrand());
        existing.setVehicleType(updatedVehicle.getVehicleType());
        existing.setTrailerType(updatedVehicle.getTrailerType());
        existing.setModel(updatedVehicle.getModel());
        existing.setYear(updatedVehicle.getYear());
        existing.setVin(updatedVehicle.getVin());
        existing.setTechnicalCondition(updatedVehicle.getTechnicalCondition());
        return vehicleRepository.save(existing);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}

