package com.sahay.exception;


import com.sahay.dto.ErrorMessage;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorMessage> apiException(ApiException exception , WebRequest request){
        var errorMessage = new ErrorMessage("999" , exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);

    }

    @ExceptionHandler(PhoneNumberTakenException.class)
    public ResponseEntity<ErrorMessage> phoneNumberTakenException(PhoneNumberTakenException exception , WebRequest request){
        var errorMessage = new ErrorMessage("999" , exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);

    }


}
