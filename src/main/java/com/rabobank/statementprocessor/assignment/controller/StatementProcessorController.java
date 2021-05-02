package com.rabobank.statementprocessor.assignment.controller;

import com.rabobank.statementprocessor.assignment.enums.Result;
import com.rabobank.statementprocessor.assignment.model.StatementInput;
import com.rabobank.statementprocessor.assignment.model.StatementOutput;
import com.rabobank.statementprocessor.assignment.service.StatementProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("processor/v1/")
public class StatementProcessorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatementProcessorController.class);

    private StatementProcessorService statementProcessorService;

    @Autowired
    public StatementProcessorController(StatementProcessorService statementProcessorService) {
        this.statementProcessorService = statementProcessorService;
    }

    @PostMapping(value = "/customer-statement", consumes = "application/json", produces = "application/json")
    public ResponseEntity<StatementOutput> processStatement(@RequestBody StatementInput statement) {
        LOGGER.info("Customer statement processing started");
        return ResponseEntity.ok(statementProcessorService.process(statement));
    }
}