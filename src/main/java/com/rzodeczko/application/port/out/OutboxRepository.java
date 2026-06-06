package com.rzodeczko.application.port.out;

import com.rzodeczko.domain.model.Booking;

public interface OutboxRepository {
    void saveOutbox(Booking booking);

}
