package com.rzodeczko.application.port.out;

import com.rzodeczko.domain.model.Booking;
import com.rzodeczko.domain.model.Hotel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TravelRepository {
    Optional<Hotel> findHotel(Long id);
    List<Booking> findOverlapping(Long hotelId, LocalDate start, LocalDate end);
    Booking save(Booking booking);
    void saveOutbox(Booking booking);

    void forceOptimisticLocking(Hotel hotel);
}
