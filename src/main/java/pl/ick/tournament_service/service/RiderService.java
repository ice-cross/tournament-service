package pl.ick.tournament_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ick.tournament_service.entity.AgeGroup;
import pl.ick.tournament_service.entity.Rider;
import pl.ick.tournament_service.entity.Tournament;
import pl.ick.tournament_service.entity.TournamentRider;
import pl.ick.tournament_service.exceptions.RiderNotFoundException;
import pl.ick.tournament_service.model.answer.CreateRiderAnswer;
import pl.ick.tournament_service.model.answer.EditRiderAnswer;
import pl.ick.tournament_service.model.dto.RiderDto;
import pl.ick.tournament_service.model.request.CreateRiderRequest;
import pl.ick.tournament_service.model.request.EditRiderRequest;
import pl.ick.tournament_service.repository.RiderRepository;
import pl.ick.tournament_service.utils.mappers.RiderMapper;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Transactional
public class RiderService {

    private final RiderRepository riderRepository;
    private final AgeGroupService ageGroupService;
    private final RiderMapper riderMapper;
    private final TournamentService tournamentService;

    @Transactional
    public CreateRiderAnswer createRider(CreateRiderRequest request) {
        try {
            AgeGroup ageGroup = ageGroupService.getAgeGroupById(request.ageGroupId());
            List<Tournament> tournaments = tournamentService.findAllByIds(request.tournamentIds());
            Rider rider = riderMapper.toEntity(request, ageGroup, tournaments);
            Rider saved = riderRepository.save(rider);
            tournamentService.recalculateTournamentTypes(request.tournamentIds());

            return new CreateRiderAnswer(saved.getId(), "Rider created successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error during creating rider: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<RiderDto> getAllRiders() {
        try {
            List<Rider> riders = riderRepository.findAll();

            if (!nonNull(riders) || riders.isEmpty()) {
                throw new RiderNotFoundException("No Rider found.");
            }

            return riders.stream()
                    .map(rider -> {
                        List<Long> tournamentIds = rider.getTournamentRiders().stream()
                                .map(tr -> tr.getTournament().getId())
                                .toList();

                        List<Long> confirmedTournaments = rider.getTournamentRiders().stream()
                                .filter(TournamentRider::isConfirmed)
                                .map(tr -> tr.getTournament().getId())
                                .toList();

                        return new RiderDto(
                                rider.getId(),
                                rider.getFirstName(),
                                rider.getLastName(),
                                rider.getBirthDate(),
                                rider.getAgeGroup() != null ? rider.getAgeGroup().getId() : null,
                                tournamentIds,
                                confirmedTournaments
                        );
                    })
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Error during getting riders: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<RiderDto> getConfirmedRidersDtoByTournament(Long tournamentId) {
        try {
            List<Rider> riders = riderRepository.findDistinctByTournamentRiders_Tournament_IdAndTournamentRiders_ConfirmedTrue(tournamentId);

            if (riders == null || riders.isEmpty()) {
                throw new RiderNotFoundException("No confirmed riders found for tournament ID " + tournamentId);
            }

            return riders.stream()
                    .map(rider -> {
                        List<Long> tournamentIds = rider.getTournamentRiders().stream()
                                .map(tr -> tr.getTournament().getId())
                                .toList();

                        List<Long> confirmedTournaments = rider.getTournamentRiders().stream()
                                .filter(TournamentRider::isConfirmed)
                                .map(tr -> tr.getTournament().getId())
                                .toList();

                        return new RiderDto(
                                rider.getId(),
                                rider.getFirstName(),
                                rider.getLastName(),
                                rider.getBirthDate(),
                                rider.getAgeGroup() != null ? rider.getAgeGroup().getId() : null,
                                tournamentIds,
                                confirmedTournaments
                        );
                    })
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Error during getting confirmed riders: " + e.getMessage(), e);
        }
    }

    @Transactional
    public EditRiderAnswer editRider(Long id, EditRiderRequest request) {
        try {
            Rider rider = riderRepository.findById(id)
                    .orElseThrow(() -> new RiderNotFoundException(id));

            // Update basic info
            rider.setFirstName(request.firstName());
            rider.setLastName(request.lastName());
            rider.setBirthDate(request.birthDate());

            // Update age group
            if (request.ageGroupId() != null) {
                rider.setAgeGroup(ageGroupService.getAgeGroupById(request.ageGroupId()));
            } else {
                rider.setAgeGroup(null);
            }

            // Update tournament associations (same logic as createRider)
            rider.getTournamentRiders().clear(); // remove all existing associations

            for (Long tId : request.tournamentIds()) {
                Tournament tournament = tournamentService.getTournamentById(tId);

                TournamentRider tr = new TournamentRider();
                tr.setRider(rider);
                tr.setTournament(tournament);
                tr.setConfirmed(false); // default to not confirmed
                rider.getTournamentRiders().add(tr);
            }

            Rider updated = riderRepository.save(rider);

            return new EditRiderAnswer(updated.getId(), "Rider updated successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error during updating rider: " + e.getMessage(), e);
        }
    }

    public void deleteRider(Long id) {
        try {
            Rider rider = riderRepository.findById(id)
                    .orElseThrow(() -> new RiderNotFoundException(id));

            Long tournamentId = rider.getAgeGroup().getTournament().getId();
            riderRepository.delete(rider);

            tournamentService.recalculateTournamentType(tournamentId);
        } catch (Exception e) {
            throw new RuntimeException("Error during deleting rider: {}", e) ;
        }
    }

    public Rider buildRiderFromRiderDto(RiderDto riderDto) {
        return riderMapper.fromDtoToEntity(riderDto);
    }
}

