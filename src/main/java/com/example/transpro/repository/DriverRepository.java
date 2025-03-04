package com.example.transpro.repository;

import com.example.transpro.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByTachoCardNumber(String licenseNumber);
}

