package com.rzodeczko.presentation.controller;

import com.rzodeczko.application.command.CancelBookingCommand;
import com.rzodeczko.application.port.in.CancelBookingUseCase;
import com.rzodeczko.application.port.in.CreateBookingUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookingControllerCancelTest {

    @Mock
    private CreateBookingUseCase createBookingUseCase;
    @Mock
    private CancelBookingUseCase cancelBookingUseCase;

    private BookingController controller;

    @BeforeEach
    void setUp() {
        controller = new BookingController(createBookingUseCase, cancelBookingUseCase);
    }

    @Test
    void cancel_validId_returns204NoContent() {
        ResponseEntity<Void> response = controller.cancel(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void cancel_validId_passesCorrectCommandToUseCase() {
        controller.cancel(42L);

        ArgumentCaptor<CancelBookingCommand> captor = ArgumentCaptor.forClass(CancelBookingCommand.class);
        verify(cancelBookingUseCase).cancelBooking(captor.capture());
        assertThat(captor.getValue().bookingId()).isEqualTo(42L);
    }
}
