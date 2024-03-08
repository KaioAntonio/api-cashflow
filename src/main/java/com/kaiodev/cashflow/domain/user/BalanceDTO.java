package com.kaiodev.cashflow.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record BalanceDTO(
        @Schema(example = "300.00") Double credits,
        @Schema(example = "200.00") Double debits,
        @Schema(example = "100.00") Double balance) {
    
}
