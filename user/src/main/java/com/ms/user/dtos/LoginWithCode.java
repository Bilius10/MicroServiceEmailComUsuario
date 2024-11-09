package com.ms.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginWithCode(@NotBlank String name, @NotBlank String senha, @NotBlank String codeTemporario) {
}
