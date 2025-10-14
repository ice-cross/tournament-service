package pl.ick.tournament_service.model.dto;

public record QualificationEntryDto(
        Long id,
        Long riderId,
        String riderName,
        Integer startPosition,
        Double time1,
        Double time2,
        Double bestTime
) {}
