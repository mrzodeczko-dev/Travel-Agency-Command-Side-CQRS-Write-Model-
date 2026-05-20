package com.rzodeczko.infrastructure.persistence.adapter;

import com.rzodeczko.application.port.out.TravelRepository;
import com.rzodeczko.domain.model.Booking;
import com.rzodeczko.domain.model.Hotel;
import com.rzodeczko.infrastructure.persistence.mapper.TravelMapper;
import com.rzodeczko.infrastructure.persistence.repository.JpaBookingRepository;
import com.rzodeczko.infrastructure.persistence.repository.JpaHotelRepository;
import com.rzodeczko.infrastructure.persistence.repository.JpaOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TravelPersistenceAdapter implements TravelRepository {

    private final JpaHotelRepository jpaHotelRepository;
    private final JpaBookingRepository jpaBookingRepository;
    private final JpaOutboxRepository jpaOutboxRepository;
    private final TravelMapper travelMapper;

    @Override
    public Optional<Hotel> findHotel(Long id) {
        return jpaHotelRepository
                .findById(id)
                .map(travelMapper::toHotelDomain);
    }

    @Override
    public List<Booking> findOverlapping(Long hotelId, LocalDate start, LocalDate end) {
        return jpaBookingRepository.findPotentialConflicts(hotelId, start, end)
                .stream()
                .map(travelMapper::toBookingDomain)
                .toList();
    }

    @Override
    public Booking save(Booking booking) {
        var entity = travelMapper.toBookingEntity(booking);
        var saved = jpaBookingRepository.save(entity);
        return travelMapper.toBookingDomain(saved);
    }

    @Override
    public void saveOutbox(Booking booking) {
        var outbox = travelMapper.toOutboxEntity(booking);
        jpaOutboxRepository.save(outbox);
    }

    @Override
    public void forceOptimisticLocking(Hotel hotel) {
        var entity = jpaHotelRepository
                .findById(hotel.getId())
                .orElseThrow(() -> new IllegalStateException("Hotel missing during transaction!"));

        jpaHotelRepository.saveAndFlush(entity);
    }

}
