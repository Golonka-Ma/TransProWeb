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

    @OneToOne
    @JoinColumn(name = "truck_id")
    private Vehicle truck;

    @OneToOne
    @JoinColumn(name = "trailer_id")
    private Vehicle trailer;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    private String description;
}

