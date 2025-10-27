package pl.ick.tournament_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ick.tournament_service.entity.*;
import pl.ick.tournament_service.exceptions.TournamentNotFoundException;
import pl.ick.tournament_service.model.TournamentStatus;
import pl.ick.tournament_service.model.TournamentType;
import pl.ick.tournament_service.model.answer.CreateTournamentAnswer;
import pl.ick.tournament_service.model.answer.EditTournamentAnswer;
import pl.ick.tournament_service.model.answer.GetTournamentAnswer;
import pl.ick.tournament_service.model.dto.RiderDto;
import pl.ick.tournament_service.model.dto.TournamentDto;
import pl.ick.tournament_service.model.request.CreateTournamentRequest;
import pl.ick.tournament_service.model.request.EditTournamentRequest;
import pl.ick.tournament_service.repository.*;
import pl.ick.tournament_service.utils.mappers.RiderMapper;
import pl.ick.tournament_service.utils.mappers.TournamentMapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Transactional
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final RiderRepository riderRepository;
    private final EventService eventService;
    private final TournamentMapper tournamentMapper;
    private final HeatRepository heatRepository;
    private final RiderMapper riderMapper;
    private final AgeGroupService ageGroupService;

    public CreateTournamentAnswer createTournament(CreateTournamentRequest request) {
        try {
            Event event = eventService.getEventById(request.eventId());
            AgeGroup ageGroup = ageGroupService.getAgeGroupById(request.ageGroupId());

            Tournament tournament = tournamentMapper.toEntity(request, event, ageGroup);
            Tournament saved = tournamentRepository.save(tournament);

            return new CreateTournamentAnswer(saved.getId(), "Tournament created successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error during creating tournament: {}", e);
        }

    }

    @Transactional(readOnly = true)
    public List<TournamentDto> getAllTournaments() {
        try {
            List<Tournament> tournaments = tournamentRepository.findAll();
            if(nonNull(tournaments) && !tournaments.isEmpty()) {
                return tournaments.stream()
                        .map(tournamentMapper::toDto)
                        .toList();
            } else {
                throw new TournamentNotFoundException("No tournament found.");
            }
        }catch (Exception e) {
            throw new RuntimeException("Error during getting tournament: {}", e);
        }
    }

    @Transactional
    public List<Tournament> findAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return tournamentRepository.findAllById(ids);
    }

    @Transactional(readOnly = true)
    public List<TournamentDto> getTournamentsByEvent(Long eventId) {
        try {
            List<Tournament> tournaments = tournamentRepository.findByEventId(eventId);
            if(nonNull(tournaments) && !tournaments.isEmpty()) {
                return tournaments.stream()
                        .map(tournamentMapper::toDto)
                        .toList();
            } else {
                throw new TournamentNotFoundException("No tournament found.");
            }
        }catch (Exception e) {
            throw new RuntimeException("Error during getting tournament: {}", e);
        }
    }

    @Transactional(readOnly = true)
    public GetTournamentAnswer getTournamentInfo(Long tournamentId) {
        try {
            Tournament tournament = getTournamentById(tournamentId);
            return tournamentMapper.toGetAnswer(tournament);
        } catch (Exception e) {
            throw new RuntimeException("Error during getting tournament: {}", e);
        }
    }

    @Transactional
    public List<Tournament> getTournamentsById(List<Long> tournamentIds) {
        try {
            List<Tournament> tournaments = tournamentRepository.findByIdIn(tournamentIds);
            if(nonNull(tournaments) && !tournaments.isEmpty()) {
                return tournaments;
            } else {
                throw new TournamentNotFoundException("No tournament found.");
            }
        }catch (Exception e) {
            throw new RuntimeException("Error during getting tournament: {}", e);
        }
    }

    public EditTournamentAnswer editTournament(Long tournamentId, EditTournamentRequest request) {
        try {
            Tournament tournament = getTournamentById(tournamentId);
            tournament.setName(request.name());

            Tournament updated = tournamentRepository.save(tournament);

            return new EditTournamentAnswer(updated.getId(), "Tournament updated successfully");
        } catch (Exception e){
            throw new RuntimeException("Error during updating tournament: {}", e);
        }
    }

    public void deleteTournament(Long tournamentId) {
        try {
            Tournament tournament = getTournamentById(tournamentId);
            tournamentRepository.delete(tournament);
        } catch (Exception e) {
            throw new RuntimeException("Error during deleting tournament: {}", e) ;
        }
    }

    public RiderDto getWinner(Long tournamentId) {
        Tournament tournament = getTournamentById(tournamentId);

        if (tournament.getStatus() != TournamentStatus.FINISHED) {
            throw new IllegalStateException("Tournament not finished yet");
        }

        try {
            Heat finalHeat = heatRepository.findByTournamentId(tournamentId).stream()
                    .max(Comparator.comparingInt(Heat::getRound))
                    .orElseThrow(() -> new IllegalStateException("No heats found"));

            HeatSlot winnerSlot = finalHeat.getSlots().stream()
                    .filter(HeatSlot::isWinner)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Final heat has no winner"));

            return riderMapper.toDto(winnerSlot.getRider());
        } catch (Exception e) {
            throw new RuntimeException("Error during getting winner: {}", e);
        }
    }

    public void recalculateTournamentTypes(List<Long> tournamentIds) {
      for(Long t : tournamentIds) {
          recalculateTournamentType(t);
      }
    }

    public void recalculateTournamentType(Long tournamentId) {
        try {
            Tournament tournament = getTournamentById(tournamentId);

            long riderCount = riderRepository.countByAgeGroupTournamentId(tournamentId);

            TournamentType type;
            if (riderCount <= 32) {
                type = TournamentType.TOP32;
            } else if (riderCount <= 47) {
                type = TournamentType.TOP32_PO;
            } else {
                type = TournamentType.TOP64;
            }

            tournament.setType(type);
            tournamentRepository.save(tournament);
        } catch (Exception e){
            throw new RuntimeException("Error during recalculating tournament type: {}", e);
        }
    }

    @Transactional(readOnly = true)
    public Tournament getTournamentById(Long id){
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));
    }

    public void updateTournament(Tournament tournament){
        try {
            tournamentRepository.save(tournament);
        } catch (Exception e) {
            throw new RuntimeException("Error during updating tournament: {}", e);
        }
    }

    public Tournament saveTournament(Tournament tournament){
        return tournamentRepository.save(tournament);
    }

}
