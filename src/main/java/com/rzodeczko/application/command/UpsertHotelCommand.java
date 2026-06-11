package com.rzodeczko.application.command;

public record UpsertHotelCommand(Long hotelId, int capacity) {
}
