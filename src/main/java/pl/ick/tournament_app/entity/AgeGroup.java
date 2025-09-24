package pl.ick.tournament_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "age_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name; // e.g. "U10", "U12", "Adults"

    @Column(nullable = false)
    private int minAge;

    @Column(nullable = false)
    private int maxAge;

    @OneToOne(mappedBy = "ageGroup", fetch = FetchType.LAZY)
    private Tournament tournament;

    @OneToMany(mappedBy = "ageGroup", cascade = CascadeType.ALL)
    private List<Rider> riders = new ArrayList<>();
}
