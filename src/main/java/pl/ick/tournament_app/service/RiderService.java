package pl.ick.tournament_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ick.tournament_app.entity.AgeGroup;
import pl.ick.tournament_app.entity.Rider;
import pl.ick.tournament_app.exceptions.AgeGroupNotFoundException;
import pl.ick.tournament_app.exceptions.RiderNotFoundException;
import pl.ick.tournament_app.model.answer.CreateRiderAnswer;
import pl.ick.tournament_app.model.answer.EditRiderAnswer;
import pl.ick.tournament_app.model.answer.GetRiderAnswer;
import pl.ick.tournament_app.model.dto.RiderDto;
import pl.ick.tournament_app.model.request.CreateRiderRequest;
import pl.ick.tournament_app.model.request.EditRiderRequest;
import pl.ick.tournament_app.repository.AgeGroupRepository;
import pl.ick.tournament_app.repository.RiderRepository;
import pl.ick.tournament_app.utils.mappers.RiderMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RiderService {

    private final RiderRepository riderRepository;
    private final AgeGroupRepository ageGroupRepository;
    private final RiderMapper riderMapper;
    private final TournamentService tournamentService;

    public CreateRiderAnswer createRider(CreateRiderRequest request) {
        AgeGroup ageGroup = ageGroupRepository.findById(request.ageGroupId())
                .orElseThrow(() -> new AgeGroupNotFoundException(request.ageGroupId()));

        Rider rider = riderMapper.toEntity(request, ageGroup);
        Rider saved = riderRepository.save(rider);

        // recalc tournament type
        tournamentService.recalculateTournamentType(ageGroup.getTournament().getId());

        return new CreateRiderAnswer(saved.getId(), "Rider created successfully");
    }

    @Transactional(readOnly = true)
    public List<RiderDto> getAllRiders() {
        return riderRepository.findAll()
                .stream()
                .map(riderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RiderDto> getRidersByAgeGroup(Long ageGroupId) {
        return riderRepository.findByAgeGroupId(ageGroupId)
                .stream()
                .map(riderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RiderDto> getRidersByTournament(Long tournamentId) {
        return riderRepository.findByAgeGroupTournamentId(tournamentId)
                .stream()
                .map(riderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public GetRiderAnswer getRiderInfo(Long id) {
        Rider rider = riderRepository.findById(id)
                .orElseThrow(() -> new RiderNotFoundException(id));
        return riderMapper.toGetAnswer(rider);
    }

    public EditRiderAnswer editRider(Long id, EditRiderRequest request) {
        Rider rider = riderRepository.findById(id)
                .orElseThrow(() -> new RiderNotFoundException(id));

        rider.setFirstName(request.firstName());
        rider.setLastName(request.lastName());
        rider.setBirthDate(request.birthDate());

        Rider updated = riderRepository.save(rider);

        return new EditRiderAnswer(updated.getId(), "Rider updated successfully");
    }

    public void deleteRider(Long id) {
        Rider rider = riderRepository.findById(id)
                .orElseThrow(() -> new RiderNotFoundException(id));

        Long tournamentId = rider.getAgeGroup().getTournament().getId();
        riderRepository.delete(rider);

        // recalc tournament type
        tournamentService.recalculateTournamentType(tournamentId);
    }
}

