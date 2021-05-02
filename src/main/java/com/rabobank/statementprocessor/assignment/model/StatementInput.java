package com.rabobank.statementprocessor.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class StatementInput {
    private List<StatementRecord> input;
}
