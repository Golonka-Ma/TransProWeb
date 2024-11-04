package com.example.transpro.model;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "costs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Np. "Paliwo", "Opłata drogowa", "Serwis"

    private Double amount;

    private LocalDateTime date;

    private String description;

    // Relacja z kierowcą
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    // Relacja z pojazdem (opcjonalnie)
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    // Dodatkowe pola, np. numer dokumentu
    private String documentNumber;
}

