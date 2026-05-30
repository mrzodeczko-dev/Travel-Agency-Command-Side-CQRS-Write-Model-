package com.rzodeczko.presentation.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseDtoTest {

    @Test
    void constructor_messageOnly_setsMessageAndTimestampAndNullErrors() {
        LocalDateTime before = LocalDateTime.now();
        ErrorResponseDto dto = new ErrorResponseDto("something went wrong");
        LocalDateTime after = LocalDateTime.now();

        assertThat(dto.message()).isEqualTo("something went wrong");
        assertThat(dto.timestamp()).isBetween(before, after);
        assertThat(dto.validationErrors()).isNull();
    }

    @Test
    void constructor_messageAndErrors_setsAllFields() {
        Map<String, String> errors = Map.of("hotelId", "must not be null");
        LocalDateTime before = LocalDateTime.now();
        ErrorResponseDto dto = new ErrorResponseDto("Validation error", errors);
        LocalDateTime after = LocalDateTime.now();

        assertThat(dto.message()).isEqualTo("Validation error");
        assertThat(dto.timestamp()).isBetween(before, after);
        assertThat(dto.validationErrors()).isEqualTo(errors);
    }

    @Test
    void constructor_allArgs_storesAllFieldsAsProvided() {
        LocalDateTime timestamp = LocalDateTime.of(2027, 1, 1, 12, 0);
        Map<String, String> errors = Map.of("field", "error");

        ErrorResponseDto dto = new ErrorResponseDto("msg", timestamp, errors);

        assertThat(dto.message()).isEqualTo("msg");
        assertThat(dto.timestamp()).isEqualTo(timestamp);
        assertThat(dto.validationErrors()).isEqualTo(errors);
    }
}
