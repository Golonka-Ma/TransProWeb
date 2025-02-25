package com.example.transpro.model;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String startLocation;

    private String endLocation;

    private Double distance;

    private LocalDateTime plannedStartTime;

    private LocalDateTime plannedEndTime;

    @OneToMany(mappedBy = "route")
    private List<Load> loads;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;
}
