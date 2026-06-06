package com.rzodeczko.application.service;

import com.rzodeczko.application.command.CreateBookingCommand;
import com.rzodeczko.application.port.in.CreateBookingUseCase;
import com.rzodeczko.application.port.out.AvailabilityRepository;
import com.rzodeczko.application.port.out.BookingRepository;
import com.rzodeczko.application.port.out.HotelRepository;
import com.rzodeczko.application.port.out.OutboxRepository;
import com.rzodeczko.domain.exception.ResourceNotFoundException;
import com.rzodeczko.domain.model.Booking;
import com.rzodeczko.domain.model.Hotel;

public class BookingService implements CreateBookingUseCase {
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final AvailabilityRepository availabilityRepository;
    private final OutboxRepository outboxRepository;

    public BookingService(
            AvailabilityRepository availabilityRepository,
            HotelRepository hotelRepository,
            BookingRepository bookingRepository,
            OutboxRepository outboxRepository) {
        this.availabilityRepository = availabilityRepository;
        this.hotelRepository = hotelRepository;
        this.bookingRepository = bookingRepository;
        this.outboxRepository = outboxRepository;
    }

    @Override
    public Long createBooking(CreateBookingCommand command) {

        if (command.start().isAfter(command.end())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        Hotel hotel = hotelRepository.findHotel(command.hotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));


        availabilityRepository.reserveAvailability(
                hotel.getId(),
                hotel.getCapacity(),
                command.start(),
                command.end()
        );

        Booking newBooking = new Booking(
                null, command.hotelId(), command.userId(), command.start(), command.end());
        Booking saved = bookingRepository.save(newBooking);

        outboxRepository.saveOutbox(saved);

        return saved.id();
    }
}
