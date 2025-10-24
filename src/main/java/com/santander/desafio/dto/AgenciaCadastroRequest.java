package com.santander.desafio.dto;

import jakarta.validation.constraints.NotNull;

public record AgenciaCadastroRequest(@NotNull Double posX, @NotNull Double posY) {}
