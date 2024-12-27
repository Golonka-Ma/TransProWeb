package com.example.transpro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicle_sets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // If you only need exactly one truck & one trailer
    @OneToOne
    @JoinColumn(name = "truck_id")
    private Vehicle truck;

    @OneToOne
    @JoinColumn(name = "trailer_id")
    private Vehicle trailer;

    // If you want to reference a driver
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    // Additional fields as needed
    private String description;
    // e.g. "Truck #1 with Trailer #7"
}

