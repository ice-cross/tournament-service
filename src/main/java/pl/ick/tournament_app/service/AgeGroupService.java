package pl.ick.tournament_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ick.tournament_app.entity.AgeGroup;
import pl.ick.tournament_app.entity.Tournament;
import pl.ick.tournament_app.exceptions.AgeGroupNotFoundException;
import pl.ick.tournament_app.exceptions.TournamentNotFoundException;
import pl.ick.tournament_app.model.answer.CreateAgeGroupAnswer;
import pl.ick.tournament_app.model.answer.EditAgeGroupAnswer;
import pl.ick.tournament_app.model.answer.GetAgeGroupAnswer;
import pl.ick.tournament_app.model.dto.AgeGroupDto;
import pl.ick.tournament_app.model.request.CreateAgeGroupRequest;
import pl.ick.tournament_app.model.request.EditAgeGroupRequest;
import pl.ick.tournament_app.repository.AgeGroupRepository;
import pl.ick.tournament_app.repository.TournamentRepository;
import pl.ick.tournament_app.utils.mappers.AgeGroupMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AgeGroupService {

    private final AgeGroupRepository ageGroupRepository;
    private final TournamentRepository tournamentRepository;
    private final AgeGroupMapper ageGroupMapper;

    public CreateAgeGroupAnswer createAgeGroup(CreateAgeGroupRequest request) {

        AgeGroup ag = ageGroupMapper.toEntity(request);
        AgeGroup saved = ageGroupRepository.save(ag);

        return new CreateAgeGroupAnswer(saved.getId(), "Age group created successfully");
    }

    @Transactional(readOnly = true)
    public List<AgeGroupDto> getAllAgeGroups() {
        return ageGroupRepository.findAll()
                .stream()
                .map(ageGroupMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AgeGroupDto> getAgeGroupsByTournament(Long tournamentId) {
        return ageGroupRepository.findByTournamentId(tournamentId)
                .stream()
                .map(ageGroupMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public GetAgeGroupAnswer getAgeGroupInfo(Long id) {
        AgeGroup ag = ageGroupRepository.findById(id)
                .orElseThrow(() -> new AgeGroupNotFoundException(id));
        return ageGroupMapper.toGetAnswer(ag);
    }

    public EditAgeGroupAnswer editAgeGroup(Long id, EditAgeGroupRequest request) {
        AgeGroup ag = ageGroupRepository.findById(id)
                .orElseThrow(() -> new AgeGroupNotFoundException(id));

        ag.setName(request.name());
        ag.setMinAge(request.minAge());
        ag.setMaxAge(request.maxAge());

        AgeGroup updated = ageGroupRepository.save(ag);

        return new EditAgeGroupAnswer(updated.getId(), "Age group updated successfully");
    }

    public void deleteAgeGroup(Long id) {
        if (!ageGroupRepository.existsById(id)) {
            throw new AgeGroupNotFoundException(id);
        }
        ageGroupRepository.deleteById(id);
    }
}
