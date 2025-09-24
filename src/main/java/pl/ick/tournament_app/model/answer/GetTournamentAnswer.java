package pl.ick.tournament_app.model.answer;

public record GetTournamentAnswer(
        Long id,
        String name,
        Long eventId
) {}