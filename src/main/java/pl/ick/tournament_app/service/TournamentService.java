package pl.ick.tournament_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ick.tournament_app.entity.*;
import pl.ick.tournament_app.exceptions.AgeGroupNotFoundException;
import pl.ick.tournament_app.exceptions.EventNotFoundException;
import pl.ick.tournament_app.exceptions.TournamentNotFoundException;
import pl.ick.tournament_app.model.TournamentStatus;
import pl.ick.tournament_app.model.TournamentType;
import pl.ick.tournament_app.model.answer.CreateTournamentAnswer;
import pl.ick.tournament_app.model.answer.EditTournamentAnswer;
import pl.ick.tournament_app.model.answer.GetTournamentAnswer;
import pl.ick.tournament_app.model.dto.HeatDto;
import pl.ick.tournament_app.model.dto.RiderDto;
import pl.ick.tournament_app.model.dto.TournamentDto;
import pl.ick.tournament_app.model.request.CreateTournamentRequest;
import pl.ick.tournament_app.model.request.EditTournamentRequest;
import pl.ick.tournament_app.repository.*;
import pl.ick.tournament_app.utils.mappers.HeatMapper;
import pl.ick.tournament_app.utils.mappers.RiderMapper;
import pl.ick.tournament_app.utils.mappers.TournamentMapper;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final RiderRepository riderRepository;
    private final EventRepository eventRepository;
    private final TournamentMapper tournamentMapper;
    private final HeatRepository heatRepository;
    private final RiderMapper riderMapper;
    private final HeatMapper heatMapper;
    private final AgeGroupRepository ageGroupRepository;

    public CreateTournamentAnswer createTournament(CreateTournamentRequest request) {
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new EventNotFoundException(request.eventId()));
        AgeGroup ageGroup = ageGroupRepository.findById(request.ageGroupId())
                .orElseThrow(() -> new AgeGroupNotFoundException(request.ageGroupId()));

        Tournament tournament = tournamentMapper.toEntity(request, event, ageGroup);
        Tournament saved = tournamentRepository.save(tournament);

        return new CreateTournamentAnswer(saved.getId(), "Tournament created successfully");
    }

    @Transactional(readOnly = true)
    public List<TournamentDto> getAllTournaments() {
        return tournamentRepository.findAll()
                .stream()
                .map(tournamentMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TournamentDto> getTournamentsByEvent(Long eventId) {
        return tournamentRepository.findByEventId(eventId)
                .stream()
                .map(tournamentMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public GetTournamentAnswer getTournamentInfo(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        return tournamentMapper.toGetAnswer(tournament);
    }

    public EditTournamentAnswer editTournament(Long tournamentId, EditTournamentRequest request) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        tournament.setName(request.name());
        Tournament updated = tournamentRepository.save(tournament);

        return new EditTournamentAnswer(updated.getId(), "Tournament updated successfully");
    }

    public void deleteTournament(Long tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new TournamentNotFoundException(tournamentId);
        }
        tournamentRepository.deleteById(tournamentId);
    }

    public RiderDto getWinner(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        if (tournament.getStatus() != TournamentStatus.FINISHED) {
            throw new IllegalStateException("Tournament not finished yet");
        }

        // Final heat = max round
        Heat finalHeat = heatRepository.findByTournamentId(tournamentId).stream()
                .max(Comparator.comparingInt(Heat::getRound))
                .orElseThrow(() -> new IllegalStateException("No heats found"));

        HeatSlot winnerSlot = finalHeat.getSlots().stream()
                .filter(HeatSlot::isWinner)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Final heat has no winner"));

        return riderMapper.toDto(winnerSlot.getRider());
    }

    public List<HeatDto> getLadder(Long tournamentId) {
        return heatRepository.findByTournamentId(tournamentId).stream()
                .sorted(Comparator.comparingInt(Heat::getRound)
                        .thenComparingInt(Heat::getHeatNumber))
                .map(heatMapper::toDto)
                .toList();
    }

    public void recalculateTournamentType(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

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
    }
}
