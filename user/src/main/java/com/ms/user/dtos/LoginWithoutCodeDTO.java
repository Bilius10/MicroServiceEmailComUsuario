package com.ms.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginWithoutCodeDTO(@NotBlank String name, @NotBlank String senha) {
}
