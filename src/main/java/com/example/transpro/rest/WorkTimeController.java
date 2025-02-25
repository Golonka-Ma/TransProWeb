package com.example.transpro.rest;

import com.example.transpro.model.Driver;
import com.example.transpro.model.WorkTime;
import com.example.transpro.service.DriverService;
import com.example.transpro.service.WorkTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/worktime")
public class WorkTimeController {

    private final WorkTimeService workTimeService;
    private final DriverService driverService;

    @Autowired
    public WorkTimeController(WorkTimeService workTimeService, DriverService driverService) {
        this.workTimeService = workTimeService;
        this.driverService = driverService;
    }

    // --------------------------------------------------
    // 1) POST /api/worktime
    //    Create a daily work-time record for a driver
    // --------------------------------------------------
    @PostMapping
    public ResponseEntity<String> addDailyWorkTime(@RequestBody WorkTimeRequest request) {
        // 1) Validate driver
        Optional<Driver> driverOpt = driverService.findDriverById(request.getDriverId());
        if (driverOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Kierowca o ID: " + request.getDriverId() + " nie został znaleziony.");
        }

        // 2) Walidacja daty
        if (request.getDate() == null) {
            return ResponseEntity.badRequest().body("Data nie może być pusta.");
        }

        // 3) Podstawowe kontrole ogranicze  w oparciu o podsumowanie
        if (request.getDrivingMinutes() > 600) {
            return ResponseEntity.badRequest().body("Czas jazdy przekracza dobowy limit (10h).");
        }

        int sumaCzasuPracy = request.getDrivingMinutes() + request.getOtherWorkMinutes();

        // 15h = 900 min
        if (sumaCzasuPracy > 900) {
            return ResponseEntity.badRequest().body("Suma dobowego czasu pracy przekracza 15 godzin (900 min).");
        }

        if (request.getRestMinutes() < 540) {
            return ResponseEntity.badRequest().body("Dzienny odpoczynek musi być co najmniej 9 godzin (540 min).");
        }

        // Jeżeli jazda > 4.5h => przerwa >= 45 min
        if (request.getDrivingMinutes() > 270 && request.getBreakMinutes() < 45) {
            return ResponseEntity.badRequest().body("Niewystarczający czas przerwy (potrzebujesz 45 min).");
        }

        // 4) Build and save WorkTime object
        WorkTime workTime = WorkTime.builder()
                .driver(driverOpt.get())
                .date(request.getDate())
                .drivingMinutes(request.getDrivingMinutes())
                .breakMinutes(request.getBreakMinutes())
                .otherWorkMinutes(request.getOtherWorkMinutes())
                .restMinutes(request.getRestMinutes())
                .build();
        workTimeService.save(workTime);

        return ResponseEntity.ok("Czas pracy został zapisany pomyślnie.");
    }

    // --------------------------------------------------
    // 2) GET /api/worktime/{id}
    //    Retrieve a single record by its ID
    // --------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<WorkTime> getWorkTimeById(@PathVariable Long id) {
        Optional<WorkTime> wtOpt = workTimeService.findById(id);
        return wtOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --------------------------------------------------
    // 3) GET /api/worktime?driverId=XX&date=YYYY-MM-DD
    //    Retrieve a daily record(s) for a specific driver by date
    // --------------------------------------------------
    @GetMapping
    public ResponseEntity<List<WorkTime>> getWorkTimeByDriverAndDate(
            @RequestParam(required = false) Long driverId,
            @RequestParam(required = false) String date
    ) {
        // If no params provided, just return all records
        if (driverId == null && date == null) {
            return ResponseEntity.ok(workTimeService.findAll());
        }

        // Validate driver
        Optional<Driver> driverOpt = driverService.findDriverById(driverId);
        if (driverOpt.isEmpty()) {
            return ResponseEntity.badRequest().build(); // or a message
        }

        // Parse date
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // or a message about invalid date
        }

        // Now retrieve
        List<WorkTime> records = workTimeService.findByDriverAndDate(driverId, localDate);
        return ResponseEntity.ok(records);
    }

    // --------------------------------------------------
    // 4) GET /api/worktime/week?driverId=XX&week=NN&year=YYYY
    //    Retrieve all daily records for a given driver in a specific ISO week
    // --------------------------------------------------
    @GetMapping("/week")
    public ResponseEntity<List<WorkTime>> getWorkTimeForWeek(
            @RequestParam Long driverId,
            @RequestParam int week,
            @RequestParam int year
    ) {
        // Validate driver
        Optional<Driver> driverOpt = driverService.findDriverById(driverId);
        if (driverOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Calculate date range for the requested ISO week
        // We'll assume Monday as the first day of the week (ISO standard)
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        // Adjust for the week-based fields
        LocalDate startOfWeek = firstDayOfYear
                .with(WeekFields.ISO.weekOfWeekBasedYear(), week)
                .with(WeekFields.ISO.dayOfWeek(), 1); // Monday

        // The end of the same ISO week (Sunday)
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // Use your repository's custom method or build your own range query
        List<WorkTime> records = workTimeService.findByDriverAndDateBetween(
                driverId, startOfWeek, endOfWeek
        );
        return ResponseEntity.ok(records);
    }

    // --------------------------------------------------
    // 5) PUT /api/worktime/{id}
    //    Update an existing WorkTime record. Full update in this example.
    // --------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<String> updateWorkTime(
            @PathVariable Long id,
            @RequestBody WorkTimeRequest request
    ) {
        Optional<WorkTime> existingOpt = workTimeService.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        WorkTime existing = existingOpt.get();

        // If driver changes (rare), validate the new driver
        if (request.getDriverId() != null
                && !request.getDriverId().equals(existing.getDriver().getId())) {
            Optional<Driver> driverOpt = driverService.findDriverById(request.getDriverId());
            if (driverOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid new driver ID.");
            }
            existing.setDriver(driverOpt.get());
        }

        // Walidacja daty
        if (request.getDate() == null) {
            return ResponseEntity.badRequest().body("Data nie może być pusta.");
        }

        // Podstawowe codzienne kontrole
        if (request.getDrivingMinutes() > 600) {
            return ResponseEntity.badRequest().body("Czas jazdy przekracza dzienny limit (10h).");
        }
        int totalWork = request.getDrivingMinutes() + request.getOtherWorkMinutes();
        if (totalWork > 900) {
            return ResponseEntity.badRequest().body("Suma godzin pracy w ciągu dnia przekracza 15 godzin (900 min).");
        }
        if (request.getRestMinutes() < 540) {
            return ResponseEntity.badRequest().body("Dzienny odpoczynek musi wynosić co najmniej 9 godzin (540 min).");
        }
        if (request.getDrivingMinutes() > 270 && request.getBreakMinutes() < 45) {
            return ResponseEntity.badRequest().body("Niewystarczający czas przerwy (wymagane 45 min).");
        }

        // Update fields
        existing.setDate(request.getDate());
        existing.setDrivingMinutes(request.getDrivingMinutes());
        existing.setBreakMinutes(request.getBreakMinutes());
        existing.setOtherWorkMinutes(request.getOtherWorkMinutes());
        existing.setRestMinutes(request.getRestMinutes());

        // Save
        workTimeService.save(existing);
        return ResponseEntity.ok("Czas pracy został zaktualizowany.");
    }

    // --------------------------------------------------
    // 6) DELETE /api/worktime/{id}
    //    Delete a WorkTime record by ID
    // --------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkTime(@PathVariable Long id) {
        Optional<WorkTime> existingOpt = workTimeService.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        workTimeService.delete(id);
        return ResponseEntity.ok("Czas pracy został usunięty.");
    }

    // --------------------------------------------------
    // Request DTO
    // --------------------------------------------------
    public static class WorkTimeRequest {
        private Long driverId;
        private LocalDate date;
        private int drivingMinutes;
        private int breakMinutes;
        private int otherWorkMinutes;
        private int restMinutes;

        public Long getDriverId() {
            return driverId;
        }
        public void setDriverId(Long driverId) {
            this.driverId = driverId;
        }
        public LocalDate getDate() {
            return date;
        }
        public void setDate(LocalDate date) {
            this.date = date;
        }
        public int getDrivingMinutes() {
            return drivingMinutes;
        }
        public void setDrivingMinutes(int drivingMinutes) {
            this.drivingMinutes = drivingMinutes;
        }
        public int getBreakMinutes() {
            return breakMinutes;
        }
        public void setBreakMinutes(int breakMinutes) {
            this.breakMinutes = breakMinutes;
        }
        public int getOtherWorkMinutes() {
            return otherWorkMinutes;
        }
        public void setOtherWorkMinutes(int otherWorkMinutes) {
            this.otherWorkMinutes = otherWorkMinutes;
        }
        public int getRestMinutes() {
            return restMinutes;
        }
        public void setRestMinutes(int restMinutes) {
            this.restMinutes = restMinutes;
        }
    }
}
