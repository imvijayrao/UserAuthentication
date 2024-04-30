package com.userauth.userauthenticate.controlleradvice;

import com.userauth.userauthenticate.dtos.ErrorResponseDto;
import com.userauth.userauthenticate.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setErrorCode(HttpStatus.NOT_FOUND.value());
        errorResponseDto.setErrorMessage("USER_NOT_FOUND");
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }
}
