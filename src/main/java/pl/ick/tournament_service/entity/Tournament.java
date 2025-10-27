package pl.ick.tournament_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.ick.tournament_service.model.TournamentStatus;
import pl.ick.tournament_service.model.TournamentType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tournaments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TournamentType type = TournamentType.TOP32;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TournamentStatus status = TournamentStatus.REGISTRATION;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "age_group_id", nullable = false)
    private AgeGroup ageGroup;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<TournamentRider> tournamentRiders = new HashSet<>();
}
