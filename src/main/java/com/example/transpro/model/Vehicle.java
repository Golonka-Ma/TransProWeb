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


    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    private TrailerType trailerType;

    @ManyToMany(mappedBy = "vehicles")
    private Set<Driver> drivers;

    private String technicalCondition;

    @OneToMany(mappedBy = "vehicle")
    private List<Route> routes;
}
