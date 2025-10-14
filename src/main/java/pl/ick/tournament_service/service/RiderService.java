package pl.ick.tournament_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ick.tournament_service.entity.Rider;
import pl.ick.tournament_service.exceptions.RiderNotFoundException;
import pl.ick.tournament_service.model.answer.CreateRiderAnswer;
import pl.ick.tournament_service.model.answer.EditRiderAnswer;
import pl.ick.tournament_service.model.answer.GetRiderAnswer;
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

    public CreateRiderAnswer createRider(CreateRiderRequest request) {
        try {

            Rider rider = riderMapper.toEntity(request, ageGroupService.getAgeGroupById(request.ageGroupId()));
            Rider saved = riderRepository.save(rider);

            // recalc tournament type
            tournamentService.recalculateTournamentTypes(request.tournamentIds());

            return new CreateRiderAnswer(saved.getId(), "Rider created successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error during creating rider: {}", e);
        }
    }

    @Transactional(readOnly = true)
    public List<RiderDto> getAllRiders() {
        try {
            List<Rider> riders = riderRepository.findAll();
            if(nonNull(riders) && !riders.isEmpty()) {
                return riders.stream()
                        .map(riderMapper::toDto)
                        .toList();
            } else {
                throw new RiderNotFoundException("No Rider found.");
            }
        }catch (Exception e) {
            throw new RuntimeException("Error during getting riders: {}", e);
        }
    }

    @Transactional(readOnly = true)
    public List<RiderDto> getRidersDtoByAgeGroup(Long ageGroupId) {
        try {
            List<Rider> riders = riderRepository.findByAgeGroupId(ageGroupId);
            if(nonNull(riders) && !riders.isEmpty()) {
                return riders.stream()
                        .map(riderMapper::toDto)
                        .toList();
            } else {
                throw new RiderNotFoundException("No Rider found.");
            }
        }catch (Exception e) {
            throw new RuntimeException("Error during getting riders: {}", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Rider> getRidersByAgeGroup(Long ageGroupId) {
        try {
            List<Rider> riders = riderRepository.findByAgeGroupId(ageGroupId);
            if(nonNull(riders) && !riders.isEmpty()) {
                return riders;
            } else {
                throw new RiderNotFoundException("No Rider found.");
            }
        }catch (Exception e) {
            throw new RuntimeException("Error during getting riders: {}", e);
        }
    }

    @Transactional(readOnly = true)
    public List<RiderDto> getRidersByTournament(Long tournamentId) {
        try {
            List<Rider> riders = riderRepository.findByAgeGroupTournamentId(tournamentId);
            if(nonNull(riders) && !riders.isEmpty()) {
                return riders.stream()
                        .map(riderMapper::toDto)
                        .toList();
            } else {
                throw new RiderNotFoundException("No Rider found.");
            }
        }catch (Exception e) {
            throw new RuntimeException("Error during getting riders: {}", e);
        }
    }

    @Transactional(readOnly = true)
    public GetRiderAnswer getRiderInfo(Long id) {
        try {
            Rider rider = riderRepository.findById(id)
                    .orElseThrow(() -> new RiderNotFoundException(id));
            return riderMapper.toGetAnswer(rider);
        } catch (Exception e) {
            throw new RuntimeException("Error during getting rider: {}", e);
        }


    }

    public EditRiderAnswer editRider(Long id, EditRiderRequest request) {
        try {
            Rider rider = riderRepository.findById(id)
                    .orElseThrow(() -> new RiderNotFoundException(id));

            rider.setFirstName(request.firstName());
            rider.setLastName(request.lastName());
            rider.setBirthDate(request.birthDate());

            Rider updated = riderRepository.save(rider);

            return new EditRiderAnswer(updated.getId(), "Rider updated successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error during updating rider: {}", e);
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
}

