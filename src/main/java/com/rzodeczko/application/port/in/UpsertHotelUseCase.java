package com.rzodeczko.application.port.in;

import com.rzodeczko.application.command.UpsertHotelCommand;
import com.rzodeczko.domain.model.Hotel;

public interface UpsertHotelUseCase {
    Hotel upsertHotel(UpsertHotelCommand command);
}
