package pl.ick.tournament_app.model.dto;

import java.time.LocalDate;

public record RiderDto(
        Long id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        Long ageGroupId,
        Long tournamentId
) {}