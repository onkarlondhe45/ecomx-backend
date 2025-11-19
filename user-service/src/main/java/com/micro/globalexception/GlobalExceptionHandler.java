package com.micro.globalexception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
		ApiError apiError = ApiError.builder().status(HttpStatus.NOT_FOUND.value()).path(request.getRequestURI())
				.message(ex.getMessage()).timestamp(LocalDateTime.now()).build();
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);

	}
}
