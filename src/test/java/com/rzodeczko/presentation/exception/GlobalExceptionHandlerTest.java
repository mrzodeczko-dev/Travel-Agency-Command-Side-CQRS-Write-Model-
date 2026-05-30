package com.rzodeczko.presentation.exception;

import com.rzodeczko.domain.exception.OverbookingException;
import com.rzodeczko.domain.exception.ResourceNotFoundException;
import com.rzodeczko.presentation.dto.ErrorResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleOverbookingException_returns409WithMessage() {
        OverbookingException ex = new OverbookingException("Hotel 1 overbooked on 2027-01-10");

        ResponseEntity<ErrorResponseDto> response = handler.handleOverbookingException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Hotel 1 overbooked on 2027-01-10");
    }

    @Test
    void handleDataIntegrityViolationException_returns409WithRetryMessage() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("duplicate key");

        ResponseEntity<ErrorResponseDto> response = handler.handleDataIntegrityViolationException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Concurrent booking detected. Please retry.");
    }

    @Test
    void handlePessimisticLockingFailureException_returns409WithLockMessage() {
        PessimisticLockingFailureException ex = mock(PessimisticLockingFailureException.class);
        when(ex.getMessage()).thenReturn("lock timeout");

        ResponseEntity<ErrorResponseDto> response = handler.handlePessimisticLockingFailureException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Resource is temporarily locked. Please retry.");
    }

    @Test
    void handleResourceNotFoundException_returns404WithMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Hotel not found");

        ResponseEntity<ErrorResponseDto> response = handler.handleResourceNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Hotel not found");
    }

    @Test
    void handleIllegalArgumentException_returns400WithMessage() {
        IllegalArgumentException ex = new IllegalArgumentException("Start date cannot be after end date");

        ResponseEntity<ErrorResponseDto> response = handler.handleIllegalArgumentException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Start date cannot be after end date");
    }

    @Test
    void handleMethodArgumentNotValidException_returns400WithFieldErrors() {
        //given
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "createBookingRequestDto");
        bindingResult.addError(new FieldError("createBookingRequestDto", "hotelId", "must not be null"));
        bindingResult.addError(new FieldError("createBookingRequestDto", "start", "must be a future date"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        //when
        ResponseEntity<ErrorResponseDto> response = handler.handleMethodArgumentNotValidException(ex);


        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Validation error");
        assertThat(response.getBody().validationErrors())
                .containsEntry("hotelId", "must not be null")
                .containsEntry("start", "must be a future date");
    }

    @Test
    void handleException_returns500WithGenericMessage() {
        Exception ex = new Exception("unexpected database failure");

        ResponseEntity<ErrorResponseDto> response = handler.handleException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Internal Server Error");
    }
}
