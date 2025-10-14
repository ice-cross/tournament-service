package pl.ick.tournament_service.model.request;

import jakarta.validation.constraints.NotNull;

public record AddTimeRequest(
        @NotNull Long entryId,
        @NotNull Double timeValue
) {}
