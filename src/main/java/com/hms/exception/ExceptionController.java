package com.hms.exception;

import com.hms.responsebody.StandardErrorBody;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.util.Date;

@ControllerAdvice // Indicates that this class contains method interceptors for exceptions thrown by @RequestMapping
public class ExceptionController {
    @ExceptionHandler(value = ResourceNotFoundException.class) // Indicates that this method specifically handles exceptions thrown by request handling (@RequestMapping) methods in the same controller
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception){
        StandardErrorBody body = new StandardErrorBody(exception.generateErrorMessage(), exception.getTimestamp().toString());
        return new ResponseEntity<>(body.getContent(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalInputException.class)
    public ResponseEntity<Object> handleIllegalInputException(IllegalInputException exception){
        StandardErrorBody body = new StandardErrorBody(exception.generateErrorMessage(), exception.getTimestamp().toString());
        return new ResponseEntity<>(body.getContent(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DuplicateResourceException.class) // todo
    public ResponseEntity<Object> handleDuplicateResourceException(DuplicateResourceException exception){
        StandardErrorBody body = new StandardErrorBody(exception.generateErrorMessage(), exception.getTimestamp().toString());
        return new ResponseEntity<>(body.getContent(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception){
        StandardErrorBody body = new StandardErrorBody(exception.getMessage(), new Timestamp(new Date().getTime()).toString());
        return new ResponseEntity<>(body.getContent(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception){
        StandardErrorBody body = new StandardErrorBody(exception.getMessage(), new Timestamp(new Date().getTime()).toString());
        return new ResponseEntity<>(body.getContent(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception){
        StandardErrorBody body = new StandardErrorBody(exception.getMessage(), new Timestamp(new Date().getTime()).toString());
        return new ResponseEntity<>(body.getContent(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}