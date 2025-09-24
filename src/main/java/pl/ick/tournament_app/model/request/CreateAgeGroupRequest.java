package pl.ick.tournament_app.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateAgeGroupRequest(
        @NotBlank String name,
        @Min(0) int minAge,
        @Min(1) int maxAge
) {}