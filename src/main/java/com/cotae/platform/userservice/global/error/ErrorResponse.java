package com.cotae.platform.userservice.global.error;

import com.cotae.platform.userservice.global.error.exceptions.ArgumentError;
import com.cotae.platform.userservice.global.error.exceptions.CommonErrorCodeImpl;
import com.cotae.platform.userservice.global.error.exceptions.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private HttpStatus code;
    private String message;
    private List<ArgumentError> errors;

    public ErrorResponse(ErrorCode code, String message){
        this.code = code.getHttpStatus();
        this.message = message;
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(ErrorCode code, String message, List<ArgumentError> argumentErrors){
        this.code = code.getHttpStatus();
        this.message = message;
        this.errors = argumentErrors;
    }

    public ErrorResponse(ErrorCode code, List<ArgumentError> argumentErrors){
        this.code = code.getHttpStatus();
        this.message = code.getMessage();
        this.errors = argumentErrors;
    }

    public static ErrorResponse create(MethodArgumentTypeMismatchException e){
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        List<ArgumentError> argumentErrors = new ArrayList<>();
        argumentErrors.add(new ArgumentError(e.getName(),value,e.getErrorCode()));
        return new ErrorResponse(CommonErrorCodeImpl.INVALID_ARGUMENT_TYPE, argumentErrors);
    }
}