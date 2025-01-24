package com.example.transpro.rest;

import com.example.transpro.model.Driver;
import com.example.transpro.model.Schedule;
import com.example.transpro.service.DriverService;
import com.example.transpro.service.ScheduleService;
import com.example.transpro.type.ScheduleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

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

    // --------------------------------------------------
    // 1) GET all schedules (both WORK and VACATION)
    // --------------------------------------------------
    @GetMapping("/schedules")
    public List<Map<String, Object>> getAllSchedules() {
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

    // --------------------------------------------------
    // 2) POST /api/schedules => add WORK or VACATION
    // --------------------------------------------------
    @PostMapping("/schedules")
    public ResponseEntity<String> addSchedule(@RequestBody ScheduleRequest req) {
        // Validate driver
        Optional<Driver> driverOpt = driverService.findDriverById(req.getDriverId());
        if (driverOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Driver not found for ID: " + req.getDriverId());
        }

        // Basic date validation
        if (req.getStartDate() == null || req.getEndDate() == null) {
            return ResponseEntity.badRequest().body("Start date and end date cannot be null.");
        }
        if (req.getEndDate().isBefore(req.getStartDate())) {
            return ResponseEntity.badRequest().body("End date cannot be before start date.");
        }

        // Create new schedule
        Schedule schedule = new Schedule();
        schedule.setDriver(driverOpt.get());
        schedule.setStartDate(req.getStartDate());
        schedule.setEndDate(req.getEndDate());
        schedule.setColor(req.getColor() != null ? req.getColor() :
                (req.getType() == ScheduleType.VACATION ? "#ff6600" : "#33cc33")); // default colors
        schedule.setType(req.getType() != null ? req.getType() : ScheduleType.WORK);

        scheduleService.save(schedule);

        // Return a more descriptive success message
        return schedule.getType() == ScheduleType.WORK
                ? ResponseEntity.ok("Grafik pracy został dodany.")
                : ResponseEntity.ok("Urlop został dodany.");
    }

    // --------------------------------------------------
    // 3) PUT /api/schedules/{id} => edit schedule
    // --------------------------------------------------
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

        // Validate dates
        if (req.getStartDate() == null || req.getEndDate() == null) {
            return ResponseEntity.badRequest().body("Start date and end date cannot be null.");
        }
        if (req.getEndDate().isBefore(req.getStartDate())) {
            return ResponseEntity.badRequest().body("End date cannot be before start date.");
        }

        // Update fields
        existing.setStartDate(req.getStartDate());
        existing.setEndDate(req.getEndDate());

        if (req.getColor() != null) {
            existing.setColor(req.getColor());
        }
        if (req.getType() != null) {
            existing.setType(req.getType());
        }

        scheduleService.save(existing);
        return ResponseEntity.ok("Zaktualizowano grafik lub urlop.");
    }

    // --------------------------------------------------
    // 4) DELETE /api/schedules/{id}
    // --------------------------------------------------
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

    // --------------------------------------------------
    // DTO for requests
    // --------------------------------------------------
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
