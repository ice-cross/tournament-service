package pl.ick.tournament_app.model.dto;

import java.util.List;

public record HeatDto(
        Long id,
        int round,
        int heatNumber,
        boolean completed, // new
        List<HeatSlotDto> slots
) {}