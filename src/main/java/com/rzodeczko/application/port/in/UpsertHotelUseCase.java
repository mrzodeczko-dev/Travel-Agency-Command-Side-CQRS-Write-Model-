package com.rzodeczko.application.port.in;

import com.rzodeczko.application.event.HotelUpsertedPayload;
import com.rzodeczko.domain.model.Hotel;

public interface UpsertHotelUseCase {
    Hotel upsertHotel(HotelUpsertedPayload command);
}
