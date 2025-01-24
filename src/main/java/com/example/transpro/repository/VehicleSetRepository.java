package com.example.transpro.repository;

import com.example.transpro.model.VehicleSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleSetRepository extends JpaRepository<VehicleSet, Long> {
}
