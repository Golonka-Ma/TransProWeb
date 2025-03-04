package com.example.transpro.model;

import com.example.transpro.type.DrivingCategory;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String licenseNumber;

    private LocalDate licenseExpiryDate;

    @Column(unique = true, nullable = false)
    private String DriverLicenceNumber;

    private LocalDate DriverLicenceExpiryDate;

    @Column(unique = true)
    private String tachoCardNumber;

    private LocalDate tachoCardExpiryDate;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "driver_categories", joinColumns = @JoinColumn(name = "driver_id"))
    @Column(name = "category")
    private Set<DrivingCategory> categories = new HashSet<>();

    private Integer points;
    private Integer accidentsCount;
    private String restrictions;

    @ManyToMany
    @JoinTable(
            name = "drivers_vehicles",
            joinColumns = @JoinColumn(name = "driver_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id")
    )
    private Set<Vehicle> vehicles;

    @OneToMany(mappedBy = "driver")
    private List<Cost> costs;

    @OneToMany(mappedBy = "driver")
    private List<Load> loads;

    @Transient
    private String formattedLicenseExpiryDate;

    @Transient
    private String formattedTachoCardExpiryDate;
}
