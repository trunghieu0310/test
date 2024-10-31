package com.javaweb.controllerAdvisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.javaweb.error.ErrorResponse;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("Ngoại lệ đối số không hợp lệ xảy ra: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Yêu cầu không hợp lệ", Collections.singletonList(ex.getMessage())); // Use a single message
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        logger.error("Đã xảy ra lỗi không mong muốn: ", ex);
        ErrorResponse errorResponse = new ErrorResponse("Lỗi máy chủ nội bộ", Collections.singletonList("Đã xảy ra lỗi không mong muốn.")); // Use a single message
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
