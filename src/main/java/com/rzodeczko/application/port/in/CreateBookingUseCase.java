package com.rzodeczko.application.port.in;

import com.rzodeczko.application.command.CreateBookingCommand;

/** Inbound port — use case boundary. */
public interface CreateBookingUseCase {
    Long createBooking(CreateBookingCommand command);
}
