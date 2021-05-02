package com.rabobank.statementprocessor.assignment.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.rabobank.statementprocessor.assignment.enums.Result;
import com.rabobank.statementprocessor.assignment.exception.StatementProcessException;
import com.rabobank.statementprocessor.assignment.model.StatementOutput;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;

/**
 * @author Jagrati
 * The rest controller advice
 */
@ControllerAdvice(assignableTypes = StatementProcessorController.class)
public class StatementProcessorControllerAdvice {

    @ExceptionHandler(JsonMappingException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public StatementOutput badRequestHandler(final JsonMappingException ex) {
        return StatementOutput.builder()
                              .result(Result.BAD_REQUEST.name())
                              .errorRecord(
                                  Collections.emptyList())
                              .build();
    }

    @ExceptionHandler(StatementProcessException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public StatementOutput serviceExceptionHandler(final StatementProcessException ex) {
        return StatementOutput.builder()
                              .result(Result.INTERNAL_SERVER_ERROR.name())
                              .errorRecord(
                                  Collections.emptyList())
                              .build();
    }
}

