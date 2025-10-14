package pl.ick.tournament_service.model;

public enum TournamentStatus {
    REGISTRATION,     // riders signing up
    QUALIFICATION,    // qualification times being run
    HEATS,      // bracket stage (heats)
    FINISHED          // tournament is complete
}
