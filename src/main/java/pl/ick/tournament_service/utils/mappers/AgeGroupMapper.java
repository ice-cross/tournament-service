package pl.ick.tournament_service.utils.mappers;

import org.springframework.stereotype.Component;
import pl.ick.tournament_service.entity.AgeGroup;
import pl.ick.tournament_service.model.answer.GetAgeGroupAnswer;
import pl.ick.tournament_service.model.dto.AgeGroupDto;
import pl.ick.tournament_service.model.request.CreateAgeGroupRequest;

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
