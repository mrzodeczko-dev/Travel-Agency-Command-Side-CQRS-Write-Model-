package com.rzodeczko.application.service;

import com.rzodeczko.application.command.CancelBookingCommand;
import com.rzodeczko.application.port.out.AvailabilityRepository;
import com.rzodeczko.application.port.out.BookingRepository;
import com.rzodeczko.application.port.out.HotelRepository;
import com.rzodeczko.application.port.out.OutboxRepository;
import com.rzodeczko.domain.exception.BookingAlreadyCancelledException;
import com.rzodeczko.domain.exception.ResourceNotFoundException;
import com.rzodeczko.domain.model.Booking;
import com.rzodeczko.domain.model.BookingStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceCancelTest {

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
    void cancelBooking_activeBooking_releasesAvailability() {
        Booking active = new Booking(1L, 10L, 7L, START, END, BookingStatus.ACTIVE);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(active));

        bookingService.cancelBooking(new CancelBookingCommand(1L));

        verify(availabilityRepository).releaseAvailability(10L, START, END);
    }

    @Test
    void cancelBooking_activeBooking_savesCancelledBooking() {
        Booking active = new Booking(1L, 10L, 7L, START, END, BookingStatus.ACTIVE);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(active));

        bookingService.cancelBooking(new CancelBookingCommand(1L));

        Booking expected = new Booking(1L, 10L, 7L, START, END, BookingStatus.CANCELLED);
        verify(bookingRepository).save(expected);
    }

    @Test
    void cancelBooking_activeBooking_savesOutboxCancellation() {
        Booking active = new Booking(1L, 10L, 7L, START, END, BookingStatus.ACTIVE);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(active));

        bookingService.cancelBooking(new CancelBookingCommand(1L));

        verify(outboxRepository).saveOutboxCancellation(
                new Booking(1L, 10L, 7L, START, END, BookingStatus.CANCELLED));
    }

    @Test
    void cancelBooking_bookingNotFound_throwsResourceNotFoundException() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancelBooking(new CancelBookingCommand(99L)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Booking not found");
    }

    @Test
    void cancelBooking_bookingNotFound_neverReleasesOrSaves() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancelBooking(new CancelBookingCommand(99L)))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(availabilityRepository, never()).releaseAvailability(any(), any(), any());
        verify(bookingRepository, never()).save(any());
        verify(outboxRepository, never()).saveOutboxCancellation(any());
    }

    @Test
    void cancelBooking_alreadyCancelled_throwsBookingAlreadyCancelledException() {
        Booking cancelled = new Booking(1L, 10L, 7L, START, END, BookingStatus.CANCELLED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(cancelled));

        assertThatThrownBy(() -> bookingService.cancelBooking(new CancelBookingCommand(1L)))
                .isInstanceOf(BookingAlreadyCancelledException.class)
                .hasMessage("Booking 1 is already cancelled");
    }

    @Test
    void cancelBooking_alreadyCancelled_neverReleasesOrSaves() {
        Booking cancelled = new Booking(1L, 10L, 7L, START, END, BookingStatus.CANCELLED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(cancelled));

        assertThatThrownBy(() -> bookingService.cancelBooking(new CancelBookingCommand(1L)))
                .isInstanceOf(BookingAlreadyCancelledException.class);

        verify(availabilityRepository, never()).releaseAvailability(any(), any(), any());
        verify(bookingRepository, never()).save(any());
        verify(outboxRepository, never()).saveOutboxCancellation(any());
    }
}
