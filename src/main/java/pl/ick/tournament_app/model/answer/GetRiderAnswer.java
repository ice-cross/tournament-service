package pl.ick.tournament_app.model.answer;

import java.time.LocalDate;

public record GetRiderAnswer(Long id, String firstName, String lastName, LocalDate birthDate,
                             Long ageGroupId, Long tournamentId) {}