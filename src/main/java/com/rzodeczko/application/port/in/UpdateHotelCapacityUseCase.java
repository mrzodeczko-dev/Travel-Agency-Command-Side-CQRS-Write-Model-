package com.rzodeczko.application.port.in;

import com.rzodeczko.application.command.UpdateHotelCapacityCommand;
import com.rzodeczko.domain.model.Hotel;

public interface UpdateHotelCapacityUseCase {
    Hotel updateHotelCapacity(UpdateHotelCapacityCommand command);
}
