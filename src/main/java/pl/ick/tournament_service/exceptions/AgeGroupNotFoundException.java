package pl.ick.tournament_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AgeGroupNotFoundException extends RuntimeException {
    public AgeGroupNotFoundException(Long id) {
        super("Event with id " + id + " not found");
    }

    public AgeGroupNotFoundException(String message){
        super(message);
    }
}