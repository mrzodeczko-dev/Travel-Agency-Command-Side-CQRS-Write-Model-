package com.rzodeczko.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpsertHotelRequestDto(
        @NotNull(message = "Capacity is required")
        @Positive(message = "Capacity must me positive number")
        Long capacity
) {
}
