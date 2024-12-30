package com.example.transpro.rest;

import com.example.transpro.model.Driver;
import com.example.transpro.model.Schedule;
import com.example.transpro.service.DriverService;
import com.example.transpro.service.ScheduleService;
import com.example.transpro.type.ScheduleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.time.LocalDate;

/**
 * Exposes endpoints for schedules:
 *  GET /api/schedules         -> fetch all schedules
 *  POST /api/schedules        -> add a normal "WORK" schedule
 *  POST /api/vacations        -> add a "VACATION" schedule
 *  PUT  /api/schedules/{id}   -> edit an existing schedule
 *  DELETE /api/schedules/{id} -> delete a schedule
 */
@RestController
@RequestMapping("/api")
public class ScheduleRestController {

    private final ScheduleService scheduleService;
    private final DriverService driverService;

    @Autowired
    public ScheduleRestController(ScheduleService scheduleService, DriverService driverService) {
        this.scheduleService = scheduleService;
        this.driverService = driverService;
    }

    // 1) GET all schedules (both WORK and VACATION)
    @GetMapping("/schedules")
    public List<Map<String, Object>> getAllSchedules() {
        // We return a JSON list that FullCalendar can use:
        // [ { "id": ..., "driverName": ..., "startDate": "yyyy-MM-dd", "endDate": "yyyy-MM-dd", "color": "#...." }, ... ]
        List<Schedule> schedules = scheduleService.findAll();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Schedule s : schedules) {
            Map<String, Object> scheduleMap = new HashMap<>();
            scheduleMap.put("id", s.getId());
            scheduleMap.put("driverName", s.getDriver().getFirstName() + " " + s.getDriver().getLastName());
            scheduleMap.put("startDate", s.getStartDate().toString());
            // If FullCalendar uses exclusive end, add 1 day if you want inclusive
            scheduleMap.put("endDate", s.getEndDate().plusDays(1).toString());
            scheduleMap.put("color", s.getColor());
            scheduleMap.put("type", s.getType());
            response.add(scheduleMap);
        }
        return response;
    }

    // 2) POST /api/schedules => add a normal "WORK" schedule
    @PostMapping("/schedules")
    public ResponseEntity<String> addWorkSchedule(@RequestBody ScheduleRequest req) {
        Optional<Driver> driverOpt = driverService.findDriverById(req.getDriverId());
        if (driverOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Driver not found for ID: " + req.getDriverId());
        }

        Schedule schedule = new Schedule();
        schedule.setDriver(driverOpt.get());
        schedule.setStartDate(req.getStartDate());
        schedule.setEndDate(req.getEndDate());
        schedule.setColor(req.getColor());
        schedule.setType(ScheduleType.WORK);

        scheduleService.save(schedule);
        return ResponseEntity.ok("Grafik pracy został dodany");
    }

    // 3) POST /api/vacations => add a "VACATION"
    @PostMapping("/vacations")
    public ResponseEntity<String> addVacation(@RequestBody ScheduleRequest req) {
        Optional<Driver> driverOpt = driverService.findDriverById(req.getDriverId());
        if (driverOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Driver not found for ID: " + req.getDriverId());
        }

        // Possibly check for conflicts or cargo, etc.

        Schedule vacation = new Schedule();
        vacation.setDriver(driverOpt.get());
        vacation.setStartDate(req.getStartDate());
        vacation.setEndDate(req.getEndDate());
        vacation.setColor(req.getColor());
        vacation.setType(ScheduleType.VACATION);

        scheduleService.save(vacation);
        return ResponseEntity.ok("Urlop został dodany");
    }

    // 4) PUT /api/schedules/{id} => edit an existing schedule/vacation
    @PutMapping("/schedules/{id}")
    public ResponseEntity<String> editSchedule(@PathVariable Long id, @RequestBody ScheduleRequest req) {
        Optional<Schedule> existingOpt = scheduleService.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Schedule not found for ID: " + id);
        }
        Schedule existing = existingOpt.get();

        // If driverId changes (rare?), handle or disallow
        if (req.getDriverId() != null && !req.getDriverId().equals(existing.getDriver().getId())) {
            Optional<Driver> driverOpt = driverService.findDriverById(req.getDriverId());
            if (driverOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Driver not found for ID: " + req.getDriverId());
            }
            existing.setDriver(driverOpt.get());
        }

        // Update dates, color
        existing.setStartDate(req.getStartDate());
        existing.setEndDate(req.getEndDate());
        existing.setColor(req.getColor() != null ? req.getColor() : existing.getColor());
        // Keep the same type or let user specify
        if (req.getType() != null) {
            existing.setType(req.getType());
        }

        scheduleService.save(existing);
        return ResponseEntity.ok("Zaktualizowano grafik lub urlop");
    }

    // 5) DELETE /api/schedules/{id}
    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        Optional<Schedule> s = scheduleService.findById(id);
        if (s.isPresent()) {
            scheduleService.delete(id);
            return ResponseEntity.ok("Usunięto grafik/urlop o ID: " + id);
        } else {
            return ResponseEntity.badRequest().body("Nie znaleziono grafiku/urlopu o ID: " + id);
        }
    }

    // DTO for requests
    public static class ScheduleRequest {
        private Long driverId;
        private LocalDate startDate;
        private LocalDate endDate;
        private String color;
        private ScheduleType type;

        public Long getDriverId() {
            return driverId;
        }
        public void setDriverId(Long driverId) {
            this.driverId = driverId;
        }

        public LocalDate getStartDate() {
            return startDate;
        }
        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }
        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public String getColor() {
            return color;
        }
        public void setColor(String color) {
            this.color = color;
        }

        public ScheduleType getType() {
            return type;
        }
        public void setType(ScheduleType type) {
            this.type = type;
        }
    }
}