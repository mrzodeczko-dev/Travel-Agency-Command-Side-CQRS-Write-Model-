package com.rzodeczko.application.service;

import com.rzodeczko.application.command.CreateBookingCommand;
import com.rzodeczko.application.port.out.AvailabilityRepository;
import com.rzodeczko.application.port.out.BookingRepository;
import com.rzodeczko.application.port.out.HotelRepository;
import com.rzodeczko.application.port.out.OutboxRepository;
import com.rzodeczko.domain.exception.ResourceNotFoundException;
import com.rzodeczko.domain.model.Booking;
import com.rzodeczko.domain.model.Hotel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private OutboxRepository outboxRepository;

    @Mock
    private AvailabilityRepository availabilityRepository;

    @InjectMocks
    private BookingService bookingService;

    private static final LocalDate START = LocalDate.now().plusDays(1);
    private static final LocalDate END = LocalDate.now().plusDays(5);

    @Test
    void createBooking_validCommand_returnsSavedId() {
        Hotel hotel = new Hotel(1L, 10L);
        Booking saved = new Booking(42L, 1L, 7L, START, END);
        when(hotelRepository.findHotel(1L)).thenReturn(Optional.of(hotel));
        when(bookingRepository.save(any())).thenReturn(saved);

        Long result = bookingService.createBooking(new CreateBookingCommand(1L, 7L, START, END));

        assertThat(result).isEqualTo(42L);
    }

    @Test
    void createBooking_validCommand_reservesAvailabilityWithHotelCapacity() {
        Hotel hotel = new Hotel(1L, 5);
        Booking saved = new Booking(1L, 1L, 7L, START, END);
        when(hotelRepository.findHotel(1L)).thenReturn(Optional.of(hotel));
        when(bookingRepository.save(any())).thenReturn(saved);

        bookingService.createBooking(new CreateBookingCommand(1L, 7L, START, END));

        verify(availabilityRepository).reserveAvailability(1L, 5L, START, END);
    }

    @Test
    void createBooking_validCommand_savesBookingWithNullId() {
        Hotel hotel = new Hotel(1L, 10L);
        Booking saved = new Booking(99L, 1L, 3L, START, END);
        when(hotelRepository.findHotel(1L)).thenReturn(Optional.of(hotel));
        when(bookingRepository.save(any())).thenReturn(saved);

        bookingService.createBooking(new CreateBookingCommand(1L, 3L, START, END));

        verify(bookingRepository).save(new Booking(null, 1L, 3L, START, END));
    }

    @Test
    void createBooking_validCommand_savesOutboxWithSavedBooking() {
        Hotel hotel = new Hotel(1L, 10);
        Booking saved = new Booking(7L, 1L, 3L, START, END);
        when(hotelRepository.findHotel(1L)).thenReturn(Optional.of(hotel));
        when(bookingRepository.save(any())).thenReturn(saved);

        bookingService.createBooking(new CreateBookingCommand(1L, 3L, START, END));

        verify(outboxRepository).saveOutbox(saved);
    }

    @Test
    void createBooking_hotelNotFound_throwsResourceNotFoundException() {
        when(hotelRepository.findHotel(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(new CreateBookingCommand(99L, 7L, START, END)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Hotel not found");
    }

    @Test
    void createBooking_hotelNotFound_neverSavesBookingOrOutbox() {
        when(hotelRepository.findHotel(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(new CreateBookingCommand(99L, 7L, START, END)))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(bookingRepository, never()).save(any());
        verify(outboxRepository, never()).saveOutbox(any());
    }

    @Test
    void createBooking_startAfterEnd_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> bookingService.createBooking(new CreateBookingCommand(1L, 7L, END, START)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Start date cannot be after end date");
    }

    @Test
    void createBooking_startAfterEnd_neverCallsRepository() {
        assertThatThrownBy(() -> bookingService.createBooking(new CreateBookingCommand(1L, 7L, END, START)))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(bookingRepository);
        verifyNoInteractions(outboxRepository);
    }
}