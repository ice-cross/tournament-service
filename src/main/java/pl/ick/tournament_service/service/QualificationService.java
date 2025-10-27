package pl.ick.tournament_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ick.tournament_service.entity.Qualification;
import pl.ick.tournament_service.entity.QualificationEntry;
import pl.ick.tournament_service.entity.Tournament;
import pl.ick.tournament_service.exceptions.QualificationEntryNotFoundException;
import pl.ick.tournament_service.exceptions.QualificationNotFoundException;
import pl.ick.tournament_service.model.TournamentStatus;
import pl.ick.tournament_service.model.dto.QualificationDto;
import pl.ick.tournament_service.model.dto.QualificationEntryDto;
import pl.ick.tournament_service.model.dto.RiderDto;
import pl.ick.tournament_service.model.request.AddTimeRequest;
import pl.ick.tournament_service.repository.QualificationEntryRepository;
import pl.ick.tournament_service.repository.QualificationRepository;
import pl.ick.tournament_service.utils.mappers.QualificationMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QualificationService {

    private final QualificationRepository qualificationRepository;
    private final QualificationEntryRepository entryRepository;
    private final TournamentService tournamentService;
    private final QualificationMapper mapper;
    private final RiderService riderService;
    private final QualificationEntryRepository qualificationEntryRepository;

    @Transactional
    public QualificationDto getOrCreateQualification(Long tournamentId) {
        Qualification q = qualificationRepository.findByTournamentId(tournamentId)
                .orElseGet(() -> {
                    // create new qualification
                    Tournament t = tournamentService.getTournamentById(tournamentId);

                    Qualification newQ = new Qualification();
                    newQ.setTournament(t);
                    newQ.setEntries(new ArrayList<>());
                    t.setStatus(TournamentStatus.QUALIFICATION);
                    tournamentService.updateTournament(t);
                    return qualificationRepository.save(newQ);
                });

        // load all riders for the tournament’s age group
        List<RiderDto> allRiders = riderService.getConfirmedRidersDtoByTournament(tournamentId);

        // check which riders already have entries
        Set<Long> existingRiderIds = q.getEntries().stream()
                .map(entry -> entry.getRider().getId())
                .collect(Collectors.toSet());

        for (RiderDto rider : allRiders) {
            if (!existingRiderIds.contains(rider.id())) {
                QualificationEntry newEntry = new QualificationEntry();
                newEntry.setRider(riderService.buildRiderFromRiderDto(rider));
                newEntry.setQualification(q);
                newEntry.setStartPosition(null);
                qualificationEntryRepository.save(newEntry); // persist to DB
                q.getEntries().add(newEntry);
            }
        }

        List<QualificationEntryDto> entryDtos = q.getEntries().stream()
                .map(entry -> new QualificationEntryDto(
                        entry.getId(),
                        entry.getRider().getId(),
                        entry.getRider().getFirstName() + " " + entry.getRider().getLastName(),
                        entry.getStartPosition(),
                        entry.getTime1(),
                        entry.getTime2(),
                        entry.getBestTime()
                ))
                .toList();

        return new QualificationDto(
                q.getId(),
                q.getTournament().getId(),
                entryDtos
        );
    }


    public QualificationDto addTime(AddTimeRequest request) {
        QualificationEntry entry = entryRepository.findById(request.entryId())
                .orElseThrow(() -> new QualificationEntryNotFoundException(request.entryId()));

        if (entry.getTime1() == null) {
            entry.setTime1(request.timeValue());
        } else if (entry.getTime2() == null) {
            entry.setTime2(request.timeValue());
        } else {
            throw new IllegalStateException("Rider already has 2 times recorded");
        }

        entryRepository.save(entry);
        return mapper.toDto(entry.getQualification());
    }

    @Transactional(readOnly = true)
    public List<QualificationEntryDto> getRanking(Long tournamentId) {
        Qualification q = qualificationRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new QualificationNotFoundException(tournamentId));

        return q.getEntries().stream()
                .sorted(Comparator.comparing(QualificationEntry::getBestTime,
                        Comparator.nullsLast(Double::compareTo)))
                .map(e -> new QualificationEntryDto(
                        e.getId(),
                        e.getRider().getId(),
                        e.getRider().getFirstName() + " " + e.getRider().getLastName(),
                        e.getStartPosition(),
                        e.getTime1(),
                        e.getTime2(),
                        e.getBestTime()
                ))
                .toList();
    }

    public Qualification getQualificationByTournamentId(Long tournamentId){
        return qualificationRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new IllegalStateException("Qualification not found"));
    }
}

