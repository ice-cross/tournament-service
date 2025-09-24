package pl.ick.tournament_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ick.tournament_app.entity.*;
import pl.ick.tournament_app.exceptions.TournamentNotFoundException;
import pl.ick.tournament_app.model.TournamentStatus;
import pl.ick.tournament_app.model.dto.HeatDto;
import pl.ick.tournament_app.model.dto.HeatSlotDto;
import pl.ick.tournament_app.model.request.UpdateHeatResultRequest;
import pl.ick.tournament_app.repository.HeatMapRepository;
import pl.ick.tournament_app.repository.HeatRepository;
import pl.ick.tournament_app.repository.QualificationRepository;
import pl.ick.tournament_app.repository.TournamentRepository;
import pl.ick.tournament_app.utils.mappers.HeatMapper;

import java.util.*;

import static pl.ick.tournament_app.model.TournamentStatus.HEATS;

@Service
@RequiredArgsConstructor
@Transactional
public class HeatService {

    private final HeatRepository heatRepository;
    private final HeatMapRepository heatMapRepository;
    private final QualificationRepository qualificationRepository;
    private final TournamentRepository  tournamentRepository;
    private final HeatMapper heatMapper;

    public void generateHeatsForTournament(Long tournamentId) {
        Qualification qualification = qualificationRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new IllegalStateException("Qualification not found"));

        List<QualificationEntry> ranked = qualification.getEntries().stream()
                .sorted(Comparator.comparing(QualificationEntry::getBestTime,
                        Comparator.nullsLast(Double::compareTo)))
                .toList();

        List<HeatMap> mapping = heatMapRepository
                .findByTypeOrderByRoundAscHeatNumberAscSlotNumberAsc("top32");

        if(heatRepository.findByTournamentId(tournamentId).isEmpty()){
            Map<String, Heat> heats = new LinkedHashMap<>();

            for (HeatMap m : mapping) {
                String key = m.getRound() + "-" + m.getHeatNumber();
                Heat heat = heats.computeIfAbsent(key, k -> Heat.builder()
                        .round(m.getRound())
                        .heatNumber(m.getHeatNumber())
                        .tournament(qualification.getTournament())
                        .slots(new ArrayList<>())
                        .build());

                Rider rider = null;
                if (m.getQualificationPosition() != null) {
                    int idx = m.getQualificationPosition() - 1;
                    if (idx < ranked.size()) {
                        rider = ranked.get(idx).getRider();
                    }
                }

                HeatSlot slot = HeatSlot.builder()
                        .slotNumber(m.getSlotNumber())
                        .qualificationPosition(m.getQualificationPosition())
                        .fromRound(m.getFromRound())
                        .fromHeatNumber(m.getFromHeatNumber())
                        .fromPosition(m.getFromPosition())
                        .rider(rider)
                        .build();

                heat.addSlot(slot);
            }
            Tournament t = tournamentRepository.findById(tournamentId).orElseThrow(() -> new TournamentNotFoundException(tournamentId));
            t.setStatus(TournamentStatus.HEATS);
            tournamentRepository.save(t);
            heatRepository.saveAll(heats.values());
        }
    }

    public HeatDto getCurrentHeat(Long tournamentId) {
        return heatRepository.findByTournamentId(tournamentId).stream()
                .filter(h -> !h.isCompleted())
                .min(Comparator.comparingInt(Heat::getRound)
                        .thenComparingInt(Heat::getHeatNumber))
                .map(heatMapper::toDto)
                .orElseThrow(() -> new IllegalStateException("No ongoing heats"));
    }

    @Transactional(readOnly = true)
    public List<HeatDto> getHeats(Long tournamentId) {
        return heatRepository.findByTournamentId(tournamentId).stream()
                .filter(heat -> heat.getSlots() != null && !heat.getSlots().isEmpty()) // ✅ filter out empty heats
                .map(heat -> new HeatDto(
                        heat.getId(),
                        heat.getRound(),
                        heat.getHeatNumber(),
                        heat.isCompleted(),
                        heat.getSlots().stream()
                                .map(slot -> new HeatSlotDto(
                                        slot.getSlotNumber(),
                                        slot.getRider() != null ? slot.getRider().getId() : null,
                                        slot.getRider() != null ? slot.getRider().getFirstName()+""+slot.getRider().getLastName() : null,
                                        slot.getRank(),
                                        slot.isWinner()
                                ))
                                .toList()
                ))
                .toList();
    }

    public HeatDto updateHeatResults(UpdateHeatResultRequest request) {
        Heat heat = heatRepository.findById(request.heatId())
                .orElseThrow(() -> new IllegalStateException("Heat not found"));

        // update slot results
        for (UpdateHeatResultRequest.SlotResult result : request.results()) {
            heat.getSlots().stream()
                    .filter(s -> s.getSlotNumber() == result.slotNumber())
                    .findFirst()
                    .ifPresent(s -> {
                        s.setRank(result.rank());
                    });
        }

        // auto-progress winners
        propagateWinners(heat);

        return heatMapper.fromEntity(heat);
    }

    private void propagateWinners(Heat finishedHeat) {
        for (HeatSlot slot : finishedHeat.getSlots()) {
            if (slot.getRank() == null) continue;

            // find mappings for next round
            List<HeatMap> nextMappings = heatMapRepository
                    .findByTypeOrderByRoundAscHeatNumberAscSlotNumberAsc("top32").stream()
                    .filter(m -> Objects.equals(m.getFromRound(), finishedHeat.getRound()) &&
                            Objects.equals(m.getFromHeatNumber(), finishedHeat.getHeatNumber()) &&
                            Objects.equals(m.getFromPosition(), slot.getRank()))
                    .toList();

            for (HeatMap mapping : nextMappings) {
                Heat nextHeat = heatRepository.findByTournamentId(finishedHeat.getTournament().getId()).stream()
                        .filter(h -> h.getRound() == mapping.getRound() && h.getHeatNumber() == mapping.getHeatNumber())
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Next heat not found"));

                HeatSlot targetSlot = nextHeat.getSlots().stream()
                        .filter(s -> s.getSlotNumber() == mapping.getSlotNumber())
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Target slot not found"));

                targetSlot.setRider(slot.getRider());
            }
        }
    }
}
