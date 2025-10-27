package pl.ick.tournament_service.utils.mappers;

import org.springframework.stereotype.Component;
import pl.ick.tournament_service.entity.AgeGroup;
import pl.ick.tournament_service.entity.Rider;
import pl.ick.tournament_service.entity.Tournament;
import pl.ick.tournament_service.entity.TournamentRider;
import pl.ick.tournament_service.model.dto.RiderDto;
import pl.ick.tournament_service.model.request.CreateRiderRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RiderMapper {

    public Rider toEntity(CreateRiderRequest req, AgeGroup ageGroup, List<Tournament> tournaments) {
        Rider rider = Rider.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .birthDate(req.birthDate())
                .ageGroup(ageGroup)
                .build();

        if (tournaments != null && !tournaments.isEmpty()) {
            Set<TournamentRider> tournamentRiders = tournaments.stream()
                    .map(t -> {
                        TournamentRider tr = new TournamentRider();
                        tr.setRider(rider);
                        tr.setTournament(t);
                        tr.setConfirmed(false);
                        return tr;
                    })
                    .collect(Collectors.toSet());

            rider.setTournamentRiders(tournamentRiders);
        }

        return rider;
    }

    public RiderDto toDto(Rider r) {
        List<Long> tournamentIds = r.getTournamentRiders()
                .stream()
                .map(rt -> rt.getTournament().getId())
                .toList();

        List<Long> confirmedTournaments = r.getTournamentRiders()
                .stream()
                .filter(TournamentRider::isConfirmed)
                .map(rt -> rt.getTournament().getId())
                .toList();

        return new RiderDto(
                r.getId(),
                r.getFirstName(),
                r.getLastName(),
                r.getBirthDate(),
                r.getAgeGroup().getId(),
                tournamentIds,
                confirmedTournaments
        );
    }

    public Rider fromDtoToEntity(RiderDto riderDto) {
        Rider rider = new Rider();

        rider.setId(riderDto.id());
        rider.setFirstName(riderDto.firstName());
        rider.setLastName(riderDto.lastName());
        rider.setBirthDate(riderDto.birthDate());

        if (riderDto.ageGroupId() != null) {
            AgeGroup ageGroup = new AgeGroup();
            ageGroup.setId(riderDto.ageGroupId());
            rider.setAgeGroup(ageGroup);
        }

        // Map tournaments (from tournamentIds)
        if (riderDto.tournamentIds() != null && !riderDto.tournamentIds().isEmpty()) {
            List<TournamentRider> tournamentRiders = riderDto.tournamentIds().stream()
                    .map(tid -> {
                        Tournament tournament = new Tournament();
                        tournament.setId(tid);

                        TournamentRider tr = new TournamentRider();
                        tr.setRider(rider);
                        tr.setTournament(tournament);
                        tr.setConfirmed(
                                riderDto.confirmedTournaments() != null
                                        && riderDto.confirmedTournaments().contains(tid)
                        );
                        return tr;
                    })
                    .toList();

            rider.setTournamentRiders(tournamentRiders.stream().collect(Collectors.toSet()));
        }

        return rider;
    }
}
