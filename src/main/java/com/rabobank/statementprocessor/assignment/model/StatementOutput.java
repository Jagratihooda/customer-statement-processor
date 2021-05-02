package com.rabobank.statementprocessor.assignment.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class StatementOutput {
 private String result;
 private List<ErrorRecord> errorRecord;
}
