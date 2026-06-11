package com.rzodeczko.application.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CancelBookingCommandTest {

    @Test
    void constructor_validId_createsCommand() {
        CancelBookingCommand command = new CancelBookingCommand(1L);
        assertThat(command.bookingId()).isEqualTo(1L);
    }

    @Test
    void constructor_nullId_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new CancelBookingCommand(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Booking ID is required");
    }
}
