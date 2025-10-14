package pl.ick.tournament_service.utils.mappers;

import org.springframework.stereotype.Component;
import pl.ick.tournament_service.entity.AgeGroup;
import pl.ick.tournament_service.entity.Rider;
import pl.ick.tournament_service.entity.Tournament;
import pl.ick.tournament_service.model.answer.GetRiderAnswer;
import pl.ick.tournament_service.model.dto.RiderDto;
import pl.ick.tournament_service.model.request.CreateRiderRequest;

import java.util.List;
import java.util.Set;

@Component
public class RiderMapper {

    public Rider toEntity(CreateRiderRequest req, AgeGroup ageGroup) {
        return Rider.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .birthDate(req.birthDate())
                .ageGroup(ageGroup)
                .build();
    }

    public RiderDto toDto(Rider r) {
        return new RiderDto(
                r.getId(),
                r.getFirstName(),
                r.getLastName(),
                r.getBirthDate(),
                r.getAgeGroup().getId(),
                getTournamentsIds(r.getTournaments())
        );
    }

    public GetRiderAnswer toGetAnswer(Rider r) {
        return new GetRiderAnswer(
                r.getId(),
                r.getFirstName(),
                r.getLastName(),
                r.getBirthDate(),
                r.getAgeGroup().getId(),
                r.getAgeGroup().getTournament().getId()
        );
    }

    private List<Long> getTournamentsIds (Set<Tournament> tournament) {
        return tournament.stream().map(Tournament::getId).toList();
    }
}