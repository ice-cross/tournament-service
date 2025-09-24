package pl.ick.tournament_app.model.dto;

public record HeatSlotDto(
        int slotNumber,
        Long riderId,
        String riderName,
        Integer rank,
        boolean winner // new
) {}
