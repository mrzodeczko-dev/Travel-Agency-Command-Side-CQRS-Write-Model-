package com.rzodeczko.domain.model;

import com.rzodeczko.domain.exception.OverbookingException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Hotel aggregate root enforcing availability invariants.
 * Manages capacity constraints and optimistic locking via version field.
 */
public class Hotel {
    private Long id;
    private final int capacity;

    private final Long version;

    public Hotel(Long id, int capacity, Long version) {
        this.id = id;
        this.capacity = capacity;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    /**
     * O(N×D) Bucket Counting; D bounded (1-30 days) → O(N) effective.
     * Scalability: optimize locking blocks entire aggregate; Row-Level Locking alternative.
     */
    public void validateAvailability(List<Booking> existingBookings, LocalDate reqStart, LocalDate reqEnd) {
        int days = (int) ChronoUnit.DAYS.between(reqStart, reqEnd) + 1;
        int[] dailyOccupancy = new int[days];

        for (var existing : existingBookings) {
            LocalDate overlapStart = existing.start().isAfter(reqStart) ? existing.start() : reqStart;
            LocalDate overlapEnd = existing.end().isBefore(reqEnd) ? existing.end() : reqEnd;

            if (!overlapStart.isAfter(overlapEnd)) {
                int startIndex = (int) ChronoUnit.DAYS.between(reqStart, overlapStart);
                int endIndex = (int) ChronoUnit.DAYS.between(reqStart, overlapEnd);

                for (int i = startIndex; i <= endIndex; ++i) {
                    if (++dailyOccupancy[i] >= capacity) {
                        throw new OverbookingException("Hotel %d overbooked on date %s. Capacity: %d".formatted(
                                id,
                                reqStart.plusDays(i),
                                capacity
                        ));
                    }
                }
            }
        }
    }
}