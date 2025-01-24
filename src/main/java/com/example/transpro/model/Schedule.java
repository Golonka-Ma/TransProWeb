package com.example.transpro.model;

import com.example.transpro.type.ScheduleType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship to the Driver
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    private LocalDate startDate;
    private LocalDate endDate;

    private String color; // e.g. "#33cc33"

    @Enumerated(EnumType.STRING)
    private ScheduleType type; // WORK or VACATION
}
