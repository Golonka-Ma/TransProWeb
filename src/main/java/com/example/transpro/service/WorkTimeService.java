package com.example.transpro.service;

import com.example.transpro.model.WorkTime;
import com.example.transpro.repository.WorkTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WorkTimeService {

    private final WorkTimeRepository workTimeRepository;

    @Autowired
    public WorkTimeService(WorkTimeRepository workTimeRepository) {
        this.workTimeRepository = workTimeRepository;
    }

    public WorkTime save(WorkTime workTime) {
        return workTimeRepository.save(workTime);
    }

    public Optional<WorkTime> findById(Long id) {
        return workTimeRepository.findById(id);
    }

    public List<WorkTime> findAll() {
        return workTimeRepository.findAll();
    }

    public void delete(Long id) {
        workTimeRepository.deleteById(id);
    }

    public List<WorkTime> findByDriverAndDate(Long driverId, LocalDate date) {
        return workTimeRepository.findByDriverIdAndDate(driverId, date);
    }

    public List<WorkTime> findByDriverAndDateBetween(Long driverId, LocalDate start, LocalDate end) {
        return workTimeRepository.findByDriverIdAndDateBetween(driverId, start, end);
    }
}
