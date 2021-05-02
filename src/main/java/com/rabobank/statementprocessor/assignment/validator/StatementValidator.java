package com.rabobank.statementprocessor.assignment.validator;

import com.rabobank.statementprocessor.assignment.constants.ErrorMessageConstants;
import com.rabobank.statementprocessor.assignment.exception.StatementProcessException;
import com.rabobank.statementprocessor.assignment.model.ErrorRecord;
import com.rabobank.statementprocessor.assignment.model.StatementRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatementValidator {

    private StatementValidator() {

        throw new IllegalStateException("Utility class, not meant to be instantiated");
    }

    public static List<ErrorRecord> collectDuplicateTransactionReferences(List<StatementRecord> statementRecords) {

        List<ErrorRecord> errorRecords = new ArrayList<>();
        Map<Long, List<StatementRecord>> groupByReference = statementRecords.stream()
                                                                            .collect(Collectors.groupingBy(
                                                                                StatementRecord::getReference));

        groupByReference.forEach((ref, records) -> {
            if (records.size() > 1) {
                records.stream().findFirst().map(record -> errorRecords.add(createErrorRecord(record)));
            }
        });
        return errorRecords;
    }


    public static List<ErrorRecord> validateRecordEndBalance(List<StatementRecord> records) {

        return records.parallelStream()
                      .filter(StatementValidator::isEndBalanceNotCorrect)
                      .map(StatementValidator::createErrorRecord)
                      .collect(Collectors.toList());

    }

    private static ErrorRecord createErrorRecord(StatementRecord record) {
        return ErrorRecord.builder()
                          .reference(record.getReference())
                          .accountNumber(record.getAccountNumber())
                          .build();
    }

    private static boolean isEndBalanceNotCorrect(StatementRecord record) {
        if(record.getStartBalance() == null){
            throw new StatementProcessException(ErrorMessageConstants.START_BALANCE_NULL);
        }
        return !record.getStartBalance()
                      .add(record.getMutation())
                      .equals(record.getEndBalance());
    }

}
