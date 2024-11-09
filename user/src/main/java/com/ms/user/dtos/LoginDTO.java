package com.ms.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank String name, @NotBlank String senha) {
}
