package br.com.alura.AluraFake.util;

import excepion.CourseStatusException;
import excepion.TaskValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {



    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ErrorItemDTO>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorItemDTO> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorItemDTO::new)
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(TaskValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorItemDTO> handleTaskValidationException(TaskValidationException ex) {
        return ResponseEntity.badRequest().body(new ErrorItemDTO("task", ex.getMessage()));
    }


    @ExceptionHandler(CourseStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorItemDTO> handleCourseStatusException(CourseStatusException ex) {
        return ResponseEntity.badRequest().body(new ErrorItemDTO("course", ex.getMessage()));
    }

    // Para exeções genéricas
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorItemDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorItemDTO("entity", ex.getMessage()));
    }

}