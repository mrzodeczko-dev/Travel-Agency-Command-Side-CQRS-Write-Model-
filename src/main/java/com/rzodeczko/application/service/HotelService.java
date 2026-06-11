package com.rzodeczko.application.service;

import com.rzodeczko.application.command.UpsertHotelCommand;
import com.rzodeczko.application.port.in.UpsertHotelUseCase;
import com.rzodeczko.application.port.out.HotelRepository;
import com.rzodeczko.application.port.out.OutboxRepository;
import com.rzodeczko.domain.model.Hotel;

public class HotelService implements UpsertHotelUseCase {

    private final HotelRepository hotelRepository;
    private final OutboxRepository outboxRepository;

    public HotelService(HotelRepository hotelRepository, OutboxRepository outboxRepository) {
        this.hotelRepository = hotelRepository;
        this.outboxRepository = outboxRepository;
    }

    @Override
    public Hotel upsertHotel(UpsertHotelCommand command) {
        if (command.capacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }

        Hotel hotel;
        if (command.hotelId() != null) {
            hotel = hotelRepository.findHotel(command.hotelId())
                    .map(existing -> new Hotel(existing.getId(), command.capacity()))
                    .orElse(new Hotel(command.hotelId(), command.capacity()));
        } else {
            hotel = new Hotel(null, command.capacity());
        }

        Hotel saved = hotelRepository.saveHotel(hotel);
        outboxRepository.saveHotelOutbox(saved);
        return saved;
    }
}
