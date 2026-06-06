package com.rzodeczko.application.port.out;

import java.time.LocalDate;

public interface AvailabilityRepository {
    void reserveAvailability(Long hotelId, int capacity, LocalDate start, LocalDate end);
}
