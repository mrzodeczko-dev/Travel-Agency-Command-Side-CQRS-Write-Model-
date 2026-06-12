package com.rzodeczko.application.port.out;

import java.time.LocalDate;

public interface AvailabilityRepository {
    void reserveAvailability(Long hotelId, Long capacity, LocalDate start, LocalDate end);
    void releaseAvailability(Long hotelId, LocalDate start, LocalDate end);
}
