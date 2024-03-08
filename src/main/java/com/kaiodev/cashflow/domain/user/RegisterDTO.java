package com.kaiodev.cashflow.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterDTO(
        @Schema(example = "user@mail.com")
        String email,
        @Schema(example = "user")
        String name,
        @Schema(example = "1234")
        String password) {
}
