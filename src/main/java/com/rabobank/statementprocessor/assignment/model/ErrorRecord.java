package com.rabobank.statementprocessor.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRecord {
    private Long reference;
    private String accountNumber;
}
