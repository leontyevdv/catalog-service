package dev.oddsystems.microservices.catalog;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookControllerAdvice {
  @ExceptionHandler(BookNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String bookNotFoundHandler(BookNotFoundException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(BookAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  String bookAlreadyExistsHandler(BookAlreadyExistsException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleValidationExceptions(ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getConstraintViolations()
        .forEach(
            violation -> {
              String fieldName = violation.getPropertyPath().toString();
              String errorMessage = violation.getMessage();
              errors.put(fieldName, errorMessage);
            });

    return errors;
  }
}
