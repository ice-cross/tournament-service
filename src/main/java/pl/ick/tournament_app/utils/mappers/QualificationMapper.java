package pl.ick.tournament_app.utils.mappers;

import org.springframework.stereotype.Component;
import pl.ick.tournament_app.entity.Qualification;
import pl.ick.tournament_app.model.dto.QualificationDto;
import pl.ick.tournament_app.model.dto.QualificationEntryDto;

import java.util.List;

@Component
public class QualificationMapper {

    public QualificationDto toDto(Qualification q) {
        List<QualificationEntryDto> entries = q.getEntries().stream()
                .map(e -> new QualificationEntryDto(
                        e.getId(),
                        e.getRider().getId(),
                        e.getRider().getFirstName() + " " + e.getRider().getLastName(),
                        e.getStartPosition(),
                        e.getTime1(),
                        e.getTime2(),
                        e.getBestTime()
                ))
                .toList();

        return new QualificationDto(q.getId(), q.getTournament().getId(), entries);
    }
}

