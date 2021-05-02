package com.rabobank.statementprocessor.assignment.service;

import com.rabobank.statementprocessor.assignment.model.StatementInput;
import com.rabobank.statementprocessor.assignment.model.StatementOutput;

public interface StatementProcessorService {
     StatementOutput process(StatementInput inputData);
}
