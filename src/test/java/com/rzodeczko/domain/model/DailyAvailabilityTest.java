package com.rzodeczko.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DailyAvailabilityTest {

    private static final LocalDate DATE = LocalDate.of(2025, 6, 10);

    @Test
    void releaseOne_occupiedRoom_decrementsOccupied() {
        DailyAvailability availability = new DailyAvailability(3, 1L, DATE);

        availability.releaseOne();

        assertThat(availability.occupiedRooms()).isEqualTo(2);
    }

    @Test
    void releaseOne_singleOccupied_goesToZero() {
        DailyAvailability availability = new DailyAvailability(1, 1L, DATE);

        availability.releaseOne();

        assertThat(availability.occupiedRooms()).isZero();
    }

    @Test
    void releaseOne_zeroOccupied_throwsIllegalStateException() {
        DailyAvailability availability = new DailyAvailability(0, 1L, DATE);

        assertThatThrownBy(availability::releaseOne)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot release room");
    }
}
