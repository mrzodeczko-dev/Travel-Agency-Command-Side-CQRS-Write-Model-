package com.rzodeczko.presentation.controller;

import com.rzodeczko.application.event.HotelUpsertedPayload;
import com.rzodeczko.application.port.in.UpsertHotelUseCase;
import com.rzodeczko.domain.model.Hotel;
import com.rzodeczko.presentation.dto.HotelResponseDto;
import com.rzodeczko.presentation.dto.UpsertHotelRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels")
@Validated
public class HotelController {

    private final UpsertHotelUseCase upsertHotelUseCase;

    public HotelController(UpsertHotelUseCase upsertHotelUseCase) {
        this.upsertHotelUseCase = upsertHotelUseCase;
    }

    @PostMapping
    public ResponseEntity<HotelResponseDto> createHotel(
            @RequestBody @Valid UpsertHotelRequestDto request) {
        Hotel hotel = upsertHotelUseCase.upsertHotel(
                new HotelUpsertedPayload(null, request.capacity()));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new HotelResponseDto(hotel.getId(), hotel.getCapacity()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponseDto> updateHotel(
            @PathVariable @Positive(message = "Hotel ID must be a positive number") Long id,
            @RequestBody @Valid UpsertHotelRequestDto request) {
        Hotel hotel = upsertHotelUseCase.upsertHotel(
                new HotelUpsertedPayload(id, request.capacity()));
        return ResponseEntity
                .ok(new HotelResponseDto(hotel.getId(), hotel.getCapacity()));
    }
}
