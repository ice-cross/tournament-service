package pl.ick.tournament_service.utils.mappers;

import org.springframework.stereotype.Component;
import pl.ick.tournament_service.entity.AgeGroup;
import pl.ick.tournament_service.entity.Event;
import pl.ick.tournament_service.entity.Tournament;
import pl.ick.tournament_service.model.answer.GetTournamentAnswer;
import pl.ick.tournament_service.model.dto.TournamentDto;
import pl.ick.tournament_service.model.request.CreateTournamentRequest;

@Component
public class TournamentMapper {

    public Tournament toEntity(CreateTournamentRequest request, Event event, AgeGroup ageGroup) {
        return Tournament.builder()
                .name(request.name())
                .event(event)
                .ageGroup(ageGroup)
                .build();
    }

    public TournamentDto toDto(Tournament t) {
        return new TournamentDto(
                t.getId(),
                t.getName(),
                t.getEvent().getId(),
                t.getAgeGroup().getId(),
                t.getType()
        );
    }

    public GetTournamentAnswer toGetAnswer(Tournament tournament) {
        return new GetTournamentAnswer(
                tournament.getId(),
                tournament.getName(),
                tournament.getEvent().getId()
        );
    }
}
