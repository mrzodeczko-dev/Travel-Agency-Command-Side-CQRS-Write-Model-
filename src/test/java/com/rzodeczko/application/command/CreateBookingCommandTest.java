package com.rzodeczko.application.command;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class CreateBookingCommandTest {

    private static final LocalDate START = LocalDate.now().plusDays(1);
    private static final LocalDate END = LocalDate.now().plusDays(5);

    @Test
    void constructor_validDates_createsCommand() {
        assertThatCode(() -> new CreateBookingCommand(1L, 1L, START, END))
                .doesNotThrowAnyException();
    }

    @Test
    void constructor_nullStart_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new CreateBookingCommand(1L, 1L, null, END))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dates required");
    }

    @Test
    void constructor_nullEnd_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new CreateBookingCommand(1L, 1L, START, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dates required");
    }

    @Test
    void constructor_bothDatesNull_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new CreateBookingCommand(1L, 1L, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dates required");
    }
}