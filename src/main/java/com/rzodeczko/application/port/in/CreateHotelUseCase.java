package com.rzodeczko.application.port.in;

import com.rzodeczko.application.command.CreateHotelCommand;
import com.rzodeczko.domain.model.Hotel;

public interface CreateHotelUseCase {
    Hotel createHotel(CreateHotelCommand command);
}
