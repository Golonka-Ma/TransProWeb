package com.example.transpro.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loads")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Load {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Double weight;

    private String pickupLocation;

    private String deliveryLocation;

    private LocalDateTime pickupDateTime;

    private LocalDateTime deliveryDateTime;

    // Relacja z kierowcą
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    // Relacja z trasą
    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    // Dodatkowe pola, np. status ładunku
    private String status;
}