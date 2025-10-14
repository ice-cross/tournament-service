package pl.ick.tournament_service.utils.mappers;

import org.springframework.stereotype.Component;
import pl.ick.tournament_service.entity.Heat;
import pl.ick.tournament_service.model.dto.HeatDto;
import pl.ick.tournament_service.model.dto.HeatSlotDto;

import java.util.List;

@Component
public class HeatMapper {

    public HeatDto toDto(Heat heat) {
        boolean isCompleted = heat.getSlots().stream().allMatch(s -> s.getRank() != null);

        List<HeatSlotDto> slotDtos = heat.getSlots().stream()
                .map(s -> new HeatSlotDto(
                        s.getSlotNumber(),
                        s.getRider() != null ? s.getRider().getId() : null,
                        s.getRider() != null ? s.getRider().getFirstName() + " " + s.getRider().getLastName() : null,
                        s.getRank(),
                        s.getRank() != null && s.getRank() == 1 // winner flag
                ))
                .toList();

        return new HeatDto(
                heat.getId(),
                heat.getRound(),
                heat.getHeatNumber(),
                isCompleted,
                slotDtos
        );
    }

    public HeatDto fromEntity(Heat heat) {
        boolean isCompleted = heat.getSlots().stream().allMatch(s -> s.getRank() != null);

        List<HeatSlotDto> slotDtos = heat.getSlots().stream()
                .map(s -> new HeatSlotDto(
                        s.getSlotNumber(),
                        s.getRider() != null ? s.getRider().getId() : null,
                        s.getRider() != null ? s.getRider().getFirstName() + " " + s.getRider().getLastName() : null,
                        s.getRank(),
                        s.getRank() != null && s.getRank() == 1 // winner flag
                ))
                .toList();

        return new HeatDto(
                heat.getId(),
                heat.getRound(),
                heat.getHeatNumber(),
                isCompleted,
                slotDtos
        );
    }
}
