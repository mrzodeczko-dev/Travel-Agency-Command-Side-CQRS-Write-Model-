package com.rzodeczko.infrastructure.tx;

import com.rzodeczko.application.command.CreateBookingCommand;
import com.rzodeczko.application.port.in.CreateBookingUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionalCreateBookingUseCaseTest {

    @Mock
    private CreateBookingUseCase inner;

    private TransactionalCreateBookingUseCase useCase;

    private static final LocalDate START = LocalDate.now().plusDays(1);
    private static final LocalDate END   = LocalDate.now().plusDays(5);

    @BeforeEach
    void setUp() {
        useCase = new TransactionalCreateBookingUseCase(inner);
    }

    @Test
    void createBooking_delegatesToInner() {
        CreateBookingCommand command = new CreateBookingCommand(1L, 2L, START, END);
        when(inner.createBooking(command)).thenReturn(99L);

        useCase.createBooking(command);

        verify(inner).createBooking(command);
    }

    @Test
    void createBooking_returnsResultFromInner() {
        CreateBookingCommand command = new CreateBookingCommand(1L, 2L, START, END);
        when(inner.createBooking(command)).thenReturn(7L);

        Long result = useCase.createBooking(command);

        assertThat(result).isEqualTo(7L);
    }
}
