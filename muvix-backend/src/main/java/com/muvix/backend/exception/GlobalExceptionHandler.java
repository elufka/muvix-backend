package com.muvix.backend.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<?> notFound(RuntimeException e){
        return error(HttpStatus.NOT_FOUND,e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    ResponseEntity<?> conflict(RuntimeException e){
        return error(HttpStatus.CONFLICT,e.getMessage());
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    ResponseEntity<?> forbidden(RuntimeException e){
        return error(HttpStatus.FORBIDDEN,e.getMessage());
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    ResponseEntity<?> unauthorized(RuntimeException e){
        return error(HttpStatus.UNAUTHORIZED,"Authentication failed");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> invalid(MethodArgumentNotValidException e){
        Map<String,String> fields=e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, f->f.getDefaultMessage()==null?"Invalid value":f.getDefaultMessage(),(a,b)->a));
        return ResponseEntity.badRequest().body(Map.of("timestamp",Instant.now(),"status",400,"error","Validation failed","fields",fields));
    }

    private ResponseEntity<?> error(HttpStatus status,String message){
        return ResponseEntity.status(status).body(Map.of("timestamp",Instant.now(),"status",status.value(),"error",message));
    }
}
