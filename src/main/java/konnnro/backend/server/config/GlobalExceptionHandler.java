package konnnro.backend.server.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleError(MethodArgumentNotValidException ex) {
    Map<String, String> body = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> {
      body.put(error.getField(), error.getDefaultMessage());
    });

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
}