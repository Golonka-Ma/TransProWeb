package com.example.transpro.service;

import com.example.transpro.model.Driver;
import java.util.List;
import java.util.Optional;

public interface DriverService {
    List<Driver> findAllDrivers();
    void saveDriver(Driver driver);
    void deleteDriver(Long id);
    Optional<Driver> findDriverById(Long id);
    boolean existsByLicenseNumber(String licenseNumber);
    boolean existsByTachoCardNumber(String tachoCardNumber);
}
