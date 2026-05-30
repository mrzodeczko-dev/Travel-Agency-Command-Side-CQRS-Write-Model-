package com.rzodeczko.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OutboxEntityTest {

    @Test
    void incrementRetryCount_fromZero_incrementsToOne() {
        OutboxEntity entity = OutboxEntity.builder().build();

        entity.incrementRetryCount();

        assertThat(entity.getRetryCount()).isEqualTo(1);
    }

    @Test
    void incrementRetryCount_nullRetryCount_treatsAsZeroAndIncrements() {
        OutboxEntity entity = new OutboxEntity();

        entity.incrementRetryCount();

        assertThat(entity.getRetryCount()).isEqualTo(1);
    }

    @Test
    void incrementRetryCount_calledMultipleTimes_accumulatesCorrectly() {
        OutboxEntity entity = OutboxEntity.builder().build();

        entity.incrementRetryCount();
        entity.incrementRetryCount();
        entity.incrementRetryCount();

        assertThat(entity.getRetryCount()).isEqualTo(3);
    }

    @Test
    void hasExceededMaxRetries_retryCountBelowMax_returnsFalse() {
        OutboxEntity entity = OutboxEntity.builder().retryCount(2).build();

        assertThat(entity.hasExceededMaxRetries(5)).isFalse();
    }

    @Test
    void hasExceededMaxRetries_retryCountEqualsMax_returnsTrue() {
        OutboxEntity entity = OutboxEntity.builder().retryCount(5).build();

        assertThat(entity.hasExceededMaxRetries(5)).isTrue();
    }

    @Test
    void hasExceededMaxRetries_retryCountAboveMax_returnsTrue() {
        OutboxEntity entity = OutboxEntity.builder().retryCount(6).build();

        assertThat(entity.hasExceededMaxRetries(5)).isTrue();
    }

    @Test
    void hasExceededMaxRetries_nullRetryCount_returnsFalse() {
        OutboxEntity entity = new OutboxEntity();

        assertThat(entity.hasExceededMaxRetries(5)).isFalse();
    }
}