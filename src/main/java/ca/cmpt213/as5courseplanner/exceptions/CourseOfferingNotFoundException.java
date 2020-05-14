package ca.cmpt213.as5courseplanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception class
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CourseOfferingNotFoundException extends RuntimeException {
    public CourseOfferingNotFoundException() { }

    public CourseOfferingNotFoundException(String message) {
        super(message);
    }
}
