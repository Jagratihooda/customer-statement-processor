package com.rabobank.statementprocessor.assignment.service;

import com.rabobank.statementprocessor.assignment.enums.Result;
import com.rabobank.statementprocessor.assignment.model.StatementInput;
import com.rabobank.statementprocessor.assignment.model.StatementOutput;
import com.rabobank.statementprocessor.assignment.model.StatementRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StatementProcessorServiceTest {

    @InjectMocks
    private StatementProcessorServiceImpl service;

    @Test
    public void processSuccess() {
        StatementOutput output = service.process(mockStatementInput());
        assertEquals(0,
                     output.getErrorRecord()
                           .size());
        assertEquals(Result.SUCCESSFUL.name(), output.getResult());
    }

    @Test
    public void processWhenDuplicateReferenceExists() {
        StatementOutput output = service.process(mockDuplicateRefStatementInput());
        assertEquals(1,
                     output.getErrorRecord()
                           .size());
        assertEquals(Result.DUPLICATE_REFERENCE.name(), output.getResult());
        assertEquals("IBAN1",
                     output.getErrorRecord()
                           .get(0)
                           .getAccountNumber());
        assertEquals(new BigDecimal("1"),
                     BigDecimal.valueOf(output.getErrorRecord()
                                              .get(0)
                                              .getReference()));
    }

    @Test
    public void processWhenIncorrectEndBalanceExists() {
        StatementOutput output = service.process(mockIncorrectEndBalanceStatementInput());
        assertEquals(1,
                     output.getErrorRecord()
                           .size());
        assertEquals(Result.INCORRECT_END_BALANCE.name(), output.getResult());
        assertEquals("IBAN1",
                     output.getErrorRecord()
                           .get(0)
                           .getAccountNumber());
        assertEquals(new BigDecimal("1"),
                     BigDecimal.valueOf(output.getErrorRecord()
                                              .get(0)
                                              .getReference()));
    }

    @Test
    public void processWhenDuplicateRefAndIncorrectEndBalanceExists() {
        StatementOutput output = service.process(mockDuplicateRefAndIncorrectEndBalanceStatementInput());
        assertEquals(2,
                     output.getErrorRecord()
                           .size());
        assertEquals(Result.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE.name(), output.getResult());
        assertEquals("IBAN1",
                     output.getErrorRecord()
                           .get(0)
                           .getAccountNumber());
        assertEquals(new BigDecimal("1"),
                     BigDecimal.valueOf(output.getErrorRecord()
                                              .get(0)
                                              .getReference()));
        assertEquals("IBAN1",
                     output.getErrorRecord()
                           .get(1)
                           .getAccountNumber());
        assertEquals(new BigDecimal("1"),
                     BigDecimal.valueOf(output.getErrorRecord()
                                              .get(1)
                                              .getReference()));
    }

    private static StatementInput mockStatementInput() {
        StatementRecord firstRecord =
            StatementRecord.builder()
                           .reference(1L)
                           .accountNumber("IBAN1")
                           .startBalance(BigDecimal.valueOf(10))
                           .mutation(BigDecimal.valueOf(-5))
                           .endBalance(BigDecimal.valueOf(5))
                           .build();
        StatementRecord secondRecord =
            StatementRecord.builder()
                           .reference(2L)
                           .accountNumber("IBAN2")
                           .startBalance(BigDecimal.valueOf(10))
                           .mutation(BigDecimal.valueOf(-5))
                           .endBalance(BigDecimal.valueOf(5))
                           .build();

        return StatementInput.builder()
                             .input(Arrays.asList(firstRecord, secondRecord))
                             .build();

    }

    private static StatementInput mockDuplicateRefStatementInput() {
        StatementRecord firstRecord =
            StatementRecord.builder()
                           .reference(1L)
                           .accountNumber("IBAN1")
                           .startBalance(BigDecimal.valueOf(10))
                           .mutation(BigDecimal.valueOf(-5))
                           .endBalance(BigDecimal.valueOf(5))
                           .build();
        StatementRecord secondRecord =
            StatementRecord.builder()
                           .reference(1L)
                           .accountNumber("IBAN2")
                           .startBalance(BigDecimal.valueOf(10))
                           .mutation(BigDecimal.valueOf(-5))
                           .endBalance(BigDecimal.valueOf(5))
                           .build();

        return StatementInput.builder()
                             .input(Arrays.asList(firstRecord, secondRecord))
                             .build();

    }

    private static StatementInput mockIncorrectEndBalanceStatementInput() {
        StatementRecord firstRecord =
            StatementRecord.builder()
                           .reference(1L)
                           .accountNumber("IBAN1")
                           .startBalance(BigDecimal.valueOf(10))
                           .mutation(BigDecimal.valueOf(5))
                           .endBalance(BigDecimal.valueOf(5))
                           .build();
        StatementRecord secondRecord =
            StatementRecord.builder()
                           .reference(2L)
                           .accountNumber("IBAN2")
                           .startBalance(BigDecimal.valueOf(10))
                           .mutation(BigDecimal.valueOf(-5))
                           .endBalance(BigDecimal.valueOf(5))
                           .build();

        return StatementInput.builder()
                             .input(Arrays.asList(firstRecord, secondRecord))
                             .build();

    }

    private static StatementInput mockDuplicateRefAndIncorrectEndBalanceStatementInput() {
        StatementRecord firstRecord =
            StatementRecord.builder()
                           .reference(1L)
                           .accountNumber("IBAN1")
                           .startBalance(BigDecimal.valueOf(10))
                           .mutation(BigDecimal.valueOf(5))
                           .endBalance(BigDecimal.valueOf(5))
                           .build();
        StatementRecord secondRecord =
            StatementRecord.builder()
                           .reference(1L)
                           .accountNumber("IBAN2")
                           .startBalance(BigDecimal.valueOf(10))
                           .mutation(BigDecimal.valueOf(-5))
                           .endBalance(BigDecimal.valueOf(5))
                           .build();

        return StatementInput.builder()
                             .input(Arrays.asList(firstRecord, secondRecord))
                             .build();
    }
}
