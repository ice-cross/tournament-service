package pl.ick.tournament_app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RiderNotFoundException extends RuntimeException {
    public RiderNotFoundException(Long id) {
        super("Event with id " + id + " not found");
    }
}
