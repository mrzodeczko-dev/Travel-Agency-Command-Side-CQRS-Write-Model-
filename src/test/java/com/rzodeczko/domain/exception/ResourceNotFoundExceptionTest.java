package com.rzodeczko.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceNotFoundExceptionTest {

    @Test
    void constructor_storesMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Hotel not found");

        assertThat(ex.getMessage()).isEqualTo("Hotel not found");
    }

    @Test
    void isRuntimeException() {
        assertThat(new ResourceNotFoundException("msg")).isInstanceOf(RuntimeException.class);
    }
}
