package pl.ick.tournament_app.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTournamentRequest(
        @NotBlank String name,
        @NotNull Long eventId,
        @NotNull Long ageGroupId
) {}