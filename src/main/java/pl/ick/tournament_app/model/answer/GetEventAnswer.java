package pl.ick.tournament_app.model.answer;

import java.time.LocalDate;

public record GetEventAnswer(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        String location,
        String description
) {}