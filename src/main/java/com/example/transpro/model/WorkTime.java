package com.example.transpro.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "work_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship to a Driver
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    // The date this record applies to
    private LocalDate date;

    /**
     * Driving time in minutes.
     * For example, 9 hours => 540 minutes
     */
    private int drivingMinutes;

    /**
     * Break time in minutes.
     * E.g., 45 minutes break after 4.5h driving
     */
    private int breakMinutes;

    /**
     * Other "work" time in minutes
     * (e.g. załadunek, sprzątanie, obsługa tachografu, "młotki")
     */
    private int otherWorkMinutes;

    /**
     * Rest time in minutes (odpoczynek).
     * E.g., daily rest of 11 hours => 660 minutes
     */
    private int restMinutes;
}
