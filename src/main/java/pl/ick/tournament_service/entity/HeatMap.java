package pl.ick.tournament_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "heat_map")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeatMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // "top32", "top32po", "top64"

    private int round;
    private int heatNumber;
    private int slotNumber;

    // Qualification seeding
    private Integer qualificationPosition;

    // Auto-progression mapping
    private Integer fromRound;
    private Integer fromHeatNumber;
    private Integer fromPosition;
}

