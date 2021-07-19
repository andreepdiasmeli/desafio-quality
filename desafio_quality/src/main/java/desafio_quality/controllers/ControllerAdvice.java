package desafio_quality.controllers;

import desafio_quality.dtos.ExceptionDTO;
import desafio_quality.dtos.ErrorMessageDTO;
import desafio_quality.exceptions.PropertyHasNoRoomsException;
import desafio_quality.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({ ResourceNotFoundException.class, PropertyHasNoRoomsException.class})
    public ResponseEntity<ExceptionDTO> handleResourceExceptions(RuntimeException ex){
        return new ResponseEntity<>(new ExceptionDTO(ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<ErrorMessageDTO>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<ErrorMessageDTO>> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            List<ErrorMessageDTO> errorMessages = 
                errors.getOrDefault(fieldName, new ArrayList<>());

            errorMessages.add(new ErrorMessageDTO(errorMessage));

            errors.put(fieldName, errorMessages);
        });

        return errors;
    }
    /*

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            List<String> errorMessages =
                    errors.getOrDefault(fieldName, new ArrayList<>());

            errorMessages.add(errorMessage);

            errors.put(fieldName, errorMessages);
        });

        return errors;
    }    */

}