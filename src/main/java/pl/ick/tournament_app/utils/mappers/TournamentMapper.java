package pl.ick.tournament_app.utils.mappers;

import org.springframework.stereotype.Component;
import pl.ick.tournament_app.entity.AgeGroup;
import pl.ick.tournament_app.entity.Event;
import pl.ick.tournament_app.entity.Tournament;
import pl.ick.tournament_app.model.answer.GetTournamentAnswer;
import pl.ick.tournament_app.model.dto.TournamentDto;
import pl.ick.tournament_app.model.request.CreateTournamentRequest;

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
