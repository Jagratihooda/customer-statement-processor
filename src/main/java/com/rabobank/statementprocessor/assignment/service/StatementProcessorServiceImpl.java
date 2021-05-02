package com.rabobank.statementprocessor.assignment.service;

import com.rabobank.statementprocessor.assignment.enums.Result;
import com.rabobank.statementprocessor.assignment.model.ErrorRecord;
import com.rabobank.statementprocessor.assignment.model.StatementInput;
import com.rabobank.statementprocessor.assignment.model.StatementOutput;
import com.rabobank.statementprocessor.assignment.validator.StatementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class StatementProcessorServiceImpl implements StatementProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatementProcessorServiceImpl.class);

    public StatementOutput process(StatementInput inputData) {
        List<ErrorRecord> errorRecords = new ArrayList<>();
        List<ErrorRecord> errorRecordsForDuplicateTransactions =
            StatementValidator.collectDuplicateTransactionReferences(inputData.getInput());
        List<ErrorRecord> errorRecordsForIncorrectBalanceTransactions =
            StatementValidator.validateRecordEndBalance(inputData.getInput());
        errorRecords.addAll(errorRecordsForDuplicateTransactions);
        errorRecords.addAll(errorRecordsForIncorrectBalanceTransactions);
        LOGGER.info("Customer Statement is validated and the size of errorRecords is: {} ", errorRecords.size());
        return StatementOutput.builder()
                              .result(getResult(errorRecordsForDuplicateTransactions,
                                                errorRecordsForIncorrectBalanceTransactions))
                              .errorRecord(errorRecords)
                              .build();
    }

    private static String getResult(List<ErrorRecord> errorRecordsForDuplicateTransactions,
                                    List<ErrorRecord> errorRecordsForIncorrectBalanceTransactions) {
        if (!CollectionUtils.isEmpty(errorRecordsForDuplicateTransactions) &&
            !CollectionUtils.isEmpty(errorRecordsForIncorrectBalanceTransactions)) {
            return Result.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE.name();
        } else if (!CollectionUtils.isEmpty(errorRecordsForDuplicateTransactions)) {
            return Result.DUPLICATE_REFERENCE.name();
        } else if (!CollectionUtils.isEmpty(errorRecordsForIncorrectBalanceTransactions)) {
            return Result.INCORRECT_END_BALANCE.name();
        } else {
            return Result.SUCCESSFUL.name();
        }
    }
}
