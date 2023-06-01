package com.cotae.platform.userservice.global.error;

import com.cotae.platform.userservice.global.error.exceptions.CommonErrorCodeImpl;
import com.cotae.platform.userservice.global.error.exceptions.LoginExceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Default Exception Handlers.

    /**
     * Check Method Argument is valid. Include @RequestBody binding.
     * @param e MethodArgumentNotValidException
     * @return ResponseEntity<>(ErrorResponse,HttpStatus)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentException(MethodArgumentNotValidException e){
        log.error("Method arguments are not valid", e);
        final ErrorResponse response = new ErrorResponse(CommonErrorCodeImpl.INVALID_PARAMETER,e.getMessage());
        return new ResponseEntity<>(response, CommonErrorCodeImpl.INVALID_PARAMETER.getHttpStatus());
    }

    /**
     * Check Method Argument mismatch exceptions.
     * @param e : MethodArgumentTypeMismatchException
     * @return ResponseEntity<>(ErrorResponse,HttpStatus)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgsTypeMismatchException(MethodArgumentTypeMismatchException e){
        log.error("Method argument type mismatch", e);
        final ErrorResponse errorResponse = ErrorResponse.create(e);
        return new ResponseEntity<>(errorResponse,CommonErrorCodeImpl.INVALID_ARGUMENT_TYPE.getHttpStatus());
    }

    /**
     * Check Method Type mismatch exceptions.
     * @param e HttpRequestMethodNotSupportedException
     * @return ResponseEntity<>(ErrorResponse,HttpStatus)
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleRequestHttpMethodNotSupportException(HttpRequestMethodNotSupportedException e){
        log.error("Request http methods are not allowed.", e);
        final ErrorResponse errorResponse = new ErrorResponse(CommonErrorCodeImpl.METHOD_NOT_ALLOWED,CommonErrorCodeImpl.METHOD_NOT_ALLOWED.getMessage());
        return new ResponseEntity<>(errorResponse,CommonErrorCodeImpl.METHOD_NOT_ALLOWED.getHttpStatus());
    }

    /**
     * FIXME LOGIN REQUEST EXCEPTION TEMPLATES. PLZ REBUILD THIS METHODS.
     * @param LoginExceptions e
     * @return ResponseEntity<>(ErrorResponse,HttpStatus)
     */
    @ExceptionHandler(LoginExceptions.class)
    protected ResponseEntity<ErrorResponse> handleLoginRequestException(LoginExceptions e){
        log.error("Login exception.");
        final ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(),e.getErrorCode().getMessage(),e.getErrors());
        return new ResponseEntity<>(errorResponse, e.getErrorCode().getHttpStatus());
    }

}