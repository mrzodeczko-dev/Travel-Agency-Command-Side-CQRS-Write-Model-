package com.rzodeczko.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HotelTest {

    @Test
    void constructor_setsIdAndCapacity() {
        Hotel hotel = new Hotel(42L, 10);

        assertThat(hotel.getId()).isEqualTo(42L);
        assertThat(hotel.getCapacity()).isEqualTo(10);
    }

    @Test
    void constructor_zeroCapacity_allowed() {
        Hotel hotel = new Hotel(1L, 0);

        assertThat(hotel.getCapacity()).isZero();
    }
}
