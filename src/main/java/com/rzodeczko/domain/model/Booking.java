package com.rzodeczko.domain.model;

import java.time.LocalDate;

/**
 * Booking aggregate — persisted via transactional outbox.
 */
public record Booking(
        Long id,
        Long hotelId,
        Long userId,
        LocalDate start,
        LocalDate end) {
}
