package pl.ick.tournament_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "event")
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 255)
    private String location;

    private String description;

//    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private Set<Tournament> tournaments;

    @Builder.Default
    private Timestamp timestamp = Timestamp.from(ZonedDateTime.now().toInstant());
}
