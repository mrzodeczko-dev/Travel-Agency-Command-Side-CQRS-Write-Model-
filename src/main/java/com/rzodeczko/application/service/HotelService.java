package com.rzodeczko.application.service;

import com.rzodeczko.application.command.CreateHotelCommand;
import com.rzodeczko.application.command.UpdateHotelCapacityCommand;
import com.rzodeczko.application.port.in.CreateHotelUseCase;
import com.rzodeczko.application.port.in.UpdateHotelCapacityUseCase;
import com.rzodeczko.application.port.out.HotelRepository;
import com.rzodeczko.application.port.out.OutboxRepository;
import com.rzodeczko.domain.exception.ResourceNotFoundException;
import com.rzodeczko.domain.model.Hotel;

public class HotelService implements CreateHotelUseCase, UpdateHotelCapacityUseCase {

    private final HotelRepository hotelRepository;
    private final OutboxRepository outboxRepository;

    public HotelService(HotelRepository hotelRepository, OutboxRepository outboxRepository) {
        this.hotelRepository = hotelRepository;
        this.outboxRepository = outboxRepository;
    }

    @Override
    public Hotel createHotel(CreateHotelCommand command) {
        Hotel toSave = new Hotel(null, command.capacity());
        Hotel saved = hotelRepository.saveHotel(toSave);
        outboxRepository.saveHotelOutbox(saved);
        return saved;
    }

    @Override
    public Hotel updateHotelCapacity(UpdateHotelCapacityCommand command) {
        Hotel existing = hotelRepository.findHotel(command.hotelId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Hotel with id " + command.hotelId() + " not found"));

        Hotel toUpdate = new Hotel(existing.getId(), command.capacity());
        Hotel updated = hotelRepository.saveHotel(toUpdate);
        outboxRepository.saveHotelOutbox(updated);
        return updated;
    }
}
