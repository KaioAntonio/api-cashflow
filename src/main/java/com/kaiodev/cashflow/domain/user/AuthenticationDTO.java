package com.kaiodev.cashflow.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationDTO(
        @Schema(example = "user@gmail.com") String email,
        @Schema(example = "password@123") String password) {

}
