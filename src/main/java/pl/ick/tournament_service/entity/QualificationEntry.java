package pl.ick.tournament_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "qualification_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QualificationEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qualification_id", nullable = false)
    private Qualification qualification;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rider_id", nullable = false)
    private Rider rider;

    @Column
    private Integer startPosition;

    private Double time1;
    private Double time2;

    public Double getBestTime() {
        if (time1 == null && time2 == null) return null;
        if (time1 == null) return time2;
        if (time2 == null) return time1;
        return Math.min(time1, time2);
    }
}
