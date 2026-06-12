package com.rzodeczko.infrastructure.tx;

import com.rzodeczko.application.event.HotelUpsertedPayload;
import com.rzodeczko.application.port.in.UpsertHotelUseCase;
import com.rzodeczko.domain.model.Hotel;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TransactionalUpsertHotelUseCase implements UpsertHotelUseCase {

    private final UpsertHotelUseCase delegate;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Hotel upsertHotel(HotelUpsertedPayload command) {
        return delegate.upsertHotel(command);
    }
}
