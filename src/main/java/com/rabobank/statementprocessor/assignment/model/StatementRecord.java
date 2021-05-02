package com.rabobank.statementprocessor.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class StatementRecord {
    private Long reference;
    private String accountNumber;
    private String description;
    private BigDecimal startBalance;
    private BigDecimal mutation;
    private BigDecimal endBalance;


    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        StatementRecord that = (StatementRecord) obj;
        return Objects.equals(reference, that.reference);
    }

    @Override
    public int hashCode() {
        return reference != null ? reference.hashCode() : 0;
    }
}
