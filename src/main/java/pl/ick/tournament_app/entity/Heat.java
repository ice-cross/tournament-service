package pl.ick.tournament_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "heats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Heat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int round;
    private int heatNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @OneToMany(mappedBy = "heat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HeatSlot> slots = new ArrayList<>();

    public void addSlot(HeatSlot slot) {
        this.slots.add(slot);
        slot.setHeat(this);
    }

    public boolean isCompleted() {
        return slots.stream().allMatch(s -> s.getRank() != null);
    }
}
