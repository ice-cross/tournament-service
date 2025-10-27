package pl.ick.tournament_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ick.tournament_service.entity.TournamentRider;
import pl.ick.tournament_service.repository.TournamentRiderRepository;

@Service
@RequiredArgsConstructor
public class TournamentRiderService {

    private final TournamentRiderRepository tournamentRiderRepository;

    @Transactional
    public void confirmRiderForTournament(Long riderId, Long tournamentId) {
        TournamentRider tr = tournamentRiderRepository
                .findByRiderIdAndTournamentId(riderId, tournamentId)
                .orElseThrow(() -> new RuntimeException("TournamentRider not found"));

        tr.setConfirmed(true);
    }
}
