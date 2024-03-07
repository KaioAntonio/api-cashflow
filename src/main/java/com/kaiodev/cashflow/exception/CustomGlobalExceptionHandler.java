package com.kaiodev.cashflow.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException exception) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", exception.getMessage());

        return new ResponseEntity<Map<String, Object>>(body, HttpStatus.BAD_REQUEST);
    }
}
