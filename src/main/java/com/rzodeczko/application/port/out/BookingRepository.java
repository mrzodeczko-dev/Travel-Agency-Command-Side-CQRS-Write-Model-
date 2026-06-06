package com.rzodeczko.application.port.out;

import com.rzodeczko.domain.model.Booking;

public interface BookingRepository {
    Booking save(Booking booking);
}
