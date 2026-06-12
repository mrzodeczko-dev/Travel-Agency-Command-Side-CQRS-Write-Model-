package com.rzodeczko.application.command;

public record UpdateHotelCapacityCommand(Long hotelId, Long capacity) {

    public UpdateHotelCapacityCommand {
        if (hotelId == null) {
            throw new IllegalArgumentException("Hotel ID is required");
        }
        if (capacity == null || capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive number");
        }
    }
}
