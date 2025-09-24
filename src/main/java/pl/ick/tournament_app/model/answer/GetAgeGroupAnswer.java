package pl.ick.tournament_app.model.answer;

public record GetAgeGroupAnswer(Long id, String name, int minAge, int maxAge, Long tournamentId) {}
