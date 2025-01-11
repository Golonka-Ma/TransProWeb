package com.example.transpro.repository;

import com.example.transpro.model.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime, Long> {

    // Example of a custom query if you want to find a driver's records by date
    List<WorkTime> findByDriverIdAndDate(Long driverId, LocalDate date);

    // For retrieving all work times for a particular driver in a date range:
    List<WorkTime> findByDriverIdAndDateBetween(Long driverId, LocalDate start, LocalDate end);

}
