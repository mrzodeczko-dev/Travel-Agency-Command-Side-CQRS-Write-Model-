package com.rzodeczko.infrastructure.tx;

import com.rzodeczko.application.command.CreateHotelCommand;
import com.rzodeczko.application.port.in.CreateHotelUseCase;
import com.rzodeczko.domain.model.Hotel;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TransactionalCreateHotelUseCase implements CreateHotelUseCase {

    private final CreateHotelUseCase delegate;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Hotel createHotel(CreateHotelCommand command) {
        return delegate.createHotel(command);
    }
}
