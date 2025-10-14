package pl.ick.tournament_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TournamentNotFoundException extends RuntimeException {
    public TournamentNotFoundException(Long id) {
        super("Tournament with id " + id + " not found");
    }

    public TournamentNotFoundException(String message) {
        super(message);
    }
}