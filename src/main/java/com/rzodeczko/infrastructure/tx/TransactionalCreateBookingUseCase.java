package com.rzodeczko.infrastructure.tx;

import com.rzodeczko.application.command.CreateBookingCommand;
import com.rzodeczko.application.port.in.CreateBookingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TransactionalCreateBookingUseCase implements CreateBookingUseCase {

    private final CreateBookingUseCase createBookingUseCase;


    /**
     * Dlaczego isolation jest ustawione na READ_COMMITED?
     * Skoro mamy @Version nie obchodzi nas, czy ktos zmieni dane w trakcie trwania
     * naszej transakcji. Dlaczego? Bo na samym koncu przy commit, Hibernate i tak sprawdzi,
     * czy wersja hotelu jest taka sama, jak w momencie kiedy go odczytalem. Stosowanie
     * wyzszych poziomow izolacji byloby tutaj nadmiarowe.
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long createBooking(CreateBookingCommand command) {
        return createBookingUseCase.createBooking(command);
    }
}
