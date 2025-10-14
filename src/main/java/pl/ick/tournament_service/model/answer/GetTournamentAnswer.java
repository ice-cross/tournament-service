package pl.ick.tournament_service.model.answer;

public record GetTournamentAnswer(
        Long id,
        String name,
        Long eventId
) {}