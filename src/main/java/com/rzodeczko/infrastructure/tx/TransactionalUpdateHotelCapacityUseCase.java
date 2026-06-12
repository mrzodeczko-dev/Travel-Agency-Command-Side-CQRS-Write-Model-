package com.rzodeczko.infrastructure.tx;

import com.rzodeczko.application.command.UpdateHotelCapacityCommand;
import com.rzodeczko.application.port.in.UpdateHotelCapacityUseCase;
import com.rzodeczko.domain.model.Hotel;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TransactionalUpdateHotelCapacityUseCase implements UpdateHotelCapacityUseCase {

    private final UpdateHotelCapacityUseCase delegate;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Hotel updateHotelCapacity(UpdateHotelCapacityCommand command) {
        return delegate.updateHotelCapacity(command);
    }
}
