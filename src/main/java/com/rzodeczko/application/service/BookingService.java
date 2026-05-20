package com.rzodeczko.application.service;


import com.rzodeczko.application.command.CreateBookingCommand;
import com.rzodeczko.application.port.in.CreateBookingUseCase;
import com.rzodeczko.application.port.out.TravelRepository;
import com.rzodeczko.domain.exception.OverbookingException;
import com.rzodeczko.domain.model.Booking;
import com.rzodeczko.domain.model.Hotel;

/**
 * Application service implementing booking creation with domain logic and transactional outbox.
 * Orchestrates validation, persistence, and eventual consistency guarantee.
 */
public class BookingService implements CreateBookingUseCase {
    private final TravelRepository travelRepository;

    public BookingService(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    /**
     * Bucket Counting availability check → atomic write → optimistic lock verification.
     * @throws IllegalArgumentException on invalid dates or missing hotel
     * @throws OverbookingException on capacity violation
     */
    @Override
    public Long createBooking(CreateBookingCommand command) {
        // Validate date range
        if (command.start().isAfter(command.end())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        // Load hotel aggregate
        Hotel hotel = travelRepository.findHotel(command.hotelId())
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

        // Fetch conflicting bookings and validate availability
        var conflicts = travelRepository.findOverlapping(command.hotelId(), command.start(), command.end());
        hotel.validateAvailability(conflicts, command.start(), command.end());

        // Persist booking
        Booking newBooking = new Booking(
                null, command.hotelId(), command.userId(), command.start(), command.end());
        Booking saved = travelRepository.save(newBooking);

        // Transactional Outbox — guarantees eventual consistency
        travelRepository.saveOutbox(saved);

        // Optimistic locking verification
        travelRepository.forceOptimisticLocking(hotel);

        return saved.id();
    }
}
