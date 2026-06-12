package com.rzodeczko.domain.model;

public class Hotel {
    private final Long id;
    private final Long capacity;

    public Hotel(Long id, Long capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public Long getCapacity() {
        return capacity;
    }
}
