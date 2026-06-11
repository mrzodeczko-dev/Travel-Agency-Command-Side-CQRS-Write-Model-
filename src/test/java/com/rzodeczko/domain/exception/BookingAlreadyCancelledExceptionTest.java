package com.rzodeczko.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookingAlreadyCancelledExceptionTest {

    @Test
    void constructor_setsMessage() {
        var ex = new BookingAlreadyCancelledException("already cancelled");
        assertThat(ex.getMessage()).isEqualTo("already cancelled");
    }
}
