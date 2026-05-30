package com.rzodeczko.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OverbookingExceptionTest {

    @Test
    void constructor_storesMessage() {
        OverbookingException ex = new OverbookingException("Hotel 1 overbooked on 2027-01-10");

        assertThat(ex.getMessage()).isEqualTo("Hotel 1 overbooked on 2027-01-10");
    }

    @Test
    void isRuntimeException() {
        assertThat(new OverbookingException("msg")).isInstanceOf(RuntimeException.class);
    }
}
