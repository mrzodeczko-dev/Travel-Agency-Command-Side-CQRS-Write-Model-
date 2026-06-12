package com.rzodeczko.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateHotelRequestDto(
        @NotNull(message = "Capacity is required")
        @Positive(message = "Capacity must be a positive number")
        Long capacity
) {
}
