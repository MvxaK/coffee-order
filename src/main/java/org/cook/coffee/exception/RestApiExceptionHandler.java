package org.cook.coffee.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e){
        ExceptionDetails exceptionDetails = new ExceptionDetails(e.getMessage(), "Bad request", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionDetails);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException e){
        ExceptionDetails exceptionDetails = new ExceptionDetails(e.getMessage(), "Entity not found", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exceptionDetails);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleHttpClientError(HttpClientErrorException e){
        ExceptionDetails exceptionDetails = new ExceptionDetails(e.getMessage(), "Conflict error", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exceptionDetails);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<?> handleOptimisticLock(ObjectOptimisticLockingFailureException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(e.getMessage(), "Stock has changed. Please retry", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exceptionDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleInternal(Exception e){
        ExceptionDetails exceptionDetails = new ExceptionDetails(e.getMessage(), "Internal error", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionDetails);
    }

}
