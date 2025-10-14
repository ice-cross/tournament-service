package pl.ick.tournament_service.model.dto;

import java.time.LocalDate;
import java.util.List;

public record RiderDto(
        Long id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        Long ageGroupId,
        List<Long> tournamentIds
) {}