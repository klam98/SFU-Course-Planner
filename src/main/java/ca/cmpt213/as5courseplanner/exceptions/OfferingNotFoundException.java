package ca.cmpt213.as5courseplanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception class
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OfferingNotFoundException extends RuntimeException {
    public OfferingNotFoundException() { }

    public OfferingNotFoundException(String message) {
        super(message);
    }
}
