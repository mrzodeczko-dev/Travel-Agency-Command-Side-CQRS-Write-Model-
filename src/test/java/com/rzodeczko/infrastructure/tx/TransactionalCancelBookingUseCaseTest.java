package com.rzodeczko.infrastructure.tx;

import com.rzodeczko.application.command.CancelBookingCommand;
import com.rzodeczko.application.port.in.CancelBookingUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionalCancelBookingUseCaseTest {

    @Mock
    private CancelBookingUseCase inner;

    private TransactionalCancelBookingUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new TransactionalCancelBookingUseCase(inner);
    }

    @Test
    void cancelBooking_delegatesToInner() {
        CancelBookingCommand command = new CancelBookingCommand(1L);

        useCase.cancelBooking(command);

        verify(inner).cancelBooking(command);
    }
}
