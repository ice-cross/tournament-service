package pl.ick.tournament_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "heat_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeatSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int slotNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "heat_id")
    private Heat heat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private Rider rider;

    private Integer qualificationPosition;
    private Integer fromRound;
    private Integer fromHeatNumber;
    private Integer fromPosition;

    private Integer rank;     // rank within heat

    public boolean isWinner() {
        return rank != null && rank == 1;
    }
}

