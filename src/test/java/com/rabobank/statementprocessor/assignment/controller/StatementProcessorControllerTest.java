package com.rabobank.statementprocessor.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.statementprocessor.assignment.enums.Result;
import com.rabobank.statementprocessor.assignment.exception.StatementProcessException;
import com.rabobank.statementprocessor.assignment.model.ErrorRecord;
import com.rabobank.statementprocessor.assignment.model.StatementInput;
import com.rabobank.statementprocessor.assignment.model.StatementOutput;
import com.rabobank.statementprocessor.assignment.model.StatementRecord;
import com.rabobank.statementprocessor.assignment.service.StatementProcessorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class StatementProcessorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StatementProcessorServiceImpl statementProcessorServiceImpl;

    @Spy
    @InjectMocks
    private StatementProcessorController controller;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .build();
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSuccessfulProcessStatement() throws Exception {
        Mockito.when(statementProcessorServiceImpl.process(mockStatementInput()))
               .thenReturn(mockStatementOutput());

        MvcResult requestResult =
            mockMvc.perform(post("/processor/v1/customer-statement").contentType(MediaType.APPLICATION_JSON)
                                                                    .characterEncoding("utf-8")
                                                                    .content(mapper.writeValueAsString(
                                                                        mockStatementInput()))
                                                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isOk())
                   .andReturn();

        StatementOutput out = mapper.readValue(requestResult.getResponse()
                                                            .getContentAsString(),
                                               StatementOutput.class);
        assertEquals(Result.SUCCESSFUL.name(), out.getResult());
        assertEquals(1,
                     out.getErrorRecord()
                        .size());
        assertEquals("IBAN1",
                     out.getErrorRecord()
                        .get(0)
                        .getAccountNumber());

    }

    @Test
    public void testJsonMappingExceptionFailure() throws Exception {
        mockMvc.perform(post("/processor/v1/customer-statement").contentType(MediaType.APPLICATION_JSON)
                                                                .characterEncoding("utf-8")
                                                                .content(
                                                                    "{\"input\":[{\"reference\":\"1\",\"accountNumber\":\"IBAN1\"\"description\":\"First\",\"startBalance\":\"10\",\"mutation\":\"-5\",\"endBalance\":\"5\"}]}")
                                                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andReturn();

    }

    @Test
    public void testProcessStatementForGet() throws Exception {
        mockMvc.perform(get("/processor/v1/customer-statement").contentType(MediaType.APPLICATION_JSON)
                                                               .characterEncoding("utf-8")
                                                               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isMethodNotAllowed())
               .andReturn();

    }

    @Test
    public void testProcessStatementForDelete() throws Exception {
        mockMvc.perform(delete("/processor/v1/customer-statement").contentType(MediaType.APPLICATION_JSON)
                                                                  .characterEncoding("utf-8")
                                                                  .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isMethodNotAllowed())
               .andReturn();

    }

    @Test
    public void testControllerForIncorrectURL() throws Exception {
        mockMvc.perform(delete("/processor/v1/customer-statementIncorrect").contentType(MediaType.APPLICATION_JSON)
                                                                           .characterEncoding("utf-8")
                                                                           .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andReturn();

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

    private static StatementOutput mockStatementOutput() {
        return StatementOutput.builder()
                              .result(Result.SUCCESSFUL.name())
                              .errorRecord(Collections.singletonList(ErrorRecord.builder()
                                                                                .reference(1L)
                                                                                .accountNumber("IBAN1")
                                                                                .build()))
                              .build();
    }
}

