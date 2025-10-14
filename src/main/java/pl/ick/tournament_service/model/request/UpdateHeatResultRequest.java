package pl.ick.tournament_service.model.request;

import java.util.List;

public record UpdateHeatResultRequest(
        Long heatId,
        List<SlotResult> results
) {
    public record SlotResult(int slotNumber, Integer rank) {}
}
