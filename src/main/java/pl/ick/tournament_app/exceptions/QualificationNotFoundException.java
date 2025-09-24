package pl.ick.tournament_app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QualificationNotFoundException extends RuntimeException {
    public QualificationNotFoundException(Long id) {
        super("Tournament with id " + id + " not found");
    }
}