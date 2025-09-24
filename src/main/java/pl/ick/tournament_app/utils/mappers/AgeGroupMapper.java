package pl.ick.tournament_app.utils.mappers;

import org.springframework.stereotype.Component;
import pl.ick.tournament_app.entity.AgeGroup;
import pl.ick.tournament_app.entity.Tournament;
import pl.ick.tournament_app.model.answer.GetAgeGroupAnswer;
import pl.ick.tournament_app.model.dto.AgeGroupDto;
import pl.ick.tournament_app.model.request.CreateAgeGroupRequest;

import static java.util.Objects.nonNull;

@Component
public class AgeGroupMapper {

    public AgeGroup toEntity(CreateAgeGroupRequest request) {
        return AgeGroup.builder()
                .name(request.name())
                .minAge(request.minAge())
                .maxAge(request.maxAge())
                .build();
    }

    public AgeGroupDto toDto(AgeGroup ag) {
        if(nonNull(ag.getTournament())){
            return new AgeGroupDto(
                    ag.getId(),
                    ag.getName(),
                    ag.getMinAge(),
                    ag.getMaxAge(),
                    ag.getTournament().getId());
        }

        return new AgeGroupDto(
                ag.getId(),
                ag.getName(),
                ag.getMinAge(),
                ag.getMaxAge(),
                null
        );
    }

    public GetAgeGroupAnswer toGetAnswer(AgeGroup ag) {
        return new GetAgeGroupAnswer(
                ag.getId(),
                ag.getName(),
                ag.getMinAge(),
                ag.getMaxAge(),
                ag.getTournament().getId()
        );
    }
}
