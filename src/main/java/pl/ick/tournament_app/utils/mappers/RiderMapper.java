package pl.ick.tournament_app.utils.mappers;

import org.springframework.stereotype.Component;
import pl.ick.tournament_app.entity.AgeGroup;
import pl.ick.tournament_app.entity.Rider;
import pl.ick.tournament_app.model.answer.GetRiderAnswer;
import pl.ick.tournament_app.model.dto.RiderDto;
import pl.ick.tournament_app.model.request.CreateRiderRequest;

@Component
public class RiderMapper {

    public Rider toEntity(CreateRiderRequest req, AgeGroup ageGroup) {
        return Rider.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .birthDate(req.birthDate())
                .ageGroup(ageGroup)
                .build();
    }

    public RiderDto toDto(Rider r) {
        return new RiderDto(
                r.getId(),
                r.getFirstName(),
                r.getLastName(),
                r.getBirthDate(),
                r.getAgeGroup().getId(),
                r.getAgeGroup().getTournament().getId()
        );
    }

    public GetRiderAnswer toGetAnswer(Rider r) {
        return new GetRiderAnswer(
                r.getId(),
                r.getFirstName(),
                r.getLastName(),
                r.getBirthDate(),
                r.getAgeGroup().getId(),
                r.getAgeGroup().getTournament().getId()
        );
    }
}
