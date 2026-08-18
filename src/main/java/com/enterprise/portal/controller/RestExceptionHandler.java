package com.enterprise.portal.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class) ResponseEntity<Map<String,String>> notFound(Exception e){ return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage())); }
    @ExceptionHandler({IllegalArgumentException.class, AccessDeniedException.class}) ResponseEntity<Map<String,String>> bad(Exception e){ return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage())); }
}
