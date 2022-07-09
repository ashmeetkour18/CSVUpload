package com.csv.exceptions;

import com.csv.modal.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;

@RestControllerAdvice
public class CSVExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CSVException.class)
    public ResponseEntity<Response> csvExceptionHandler(CSVException csvException) {
        String exceptionMessage = csvException.getMessage();
        Response response =
               Response.builder().statusCode(HttpStatus.UNAUTHORIZED.value()).data(new ArrayList<>()).message(exceptionMessage).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
