package com.rzodeczko.application.command;

public record CreateHotelCommand(Long capacity) {

    public CreateHotelCommand {
        if (capacity == null || capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive number");
        }
    }
}
