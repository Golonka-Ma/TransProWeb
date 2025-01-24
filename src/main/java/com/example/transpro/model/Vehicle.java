package com.example.transpro.model;

import com.example.transpro.type.TrailerType;
import com.example.transpro.type.VehicleType;
import lombok.*;
import jakarta.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String registrationNumber;

    private String brand;
    private String model;
    private Integer year;

    @Column(unique = true)
    private String vin;

    // Now we add the VehicleType field
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    // TrailerType (only relevant if it's actually a trailer)
    @Enumerated(EnumType.STRING)
    private TrailerType trailerType;

    // Relacja z kierowcami (opcjonalnie)
    @ManyToMany(mappedBy = "vehicles")
    private Set<Driver> drivers;

    // Dodatkowe pola, np. stan techniczny
    private String technicalCondition;

    // Relacja z trasami (opcjonalnie)
    @OneToMany(mappedBy = "vehicle")
    private List<Route> routes;
}
