package pl.ick.tournament_app.model.dto;

import java.time.LocalDate;

public record EventDto(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        String location,
        String description
) {}