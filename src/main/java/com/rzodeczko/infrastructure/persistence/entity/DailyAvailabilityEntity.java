package com.rzodeczko.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "daily_availabilities",
        uniqueConstraints = @UniqueConstraint(columnNames = {"hotel_id", "date"})
)
@IdClass(DailyAvailabilityId.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyAvailabilityEntity {
    @Id
    @Column(name = "hotel_id")
    private Long hotelId;

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "occupied_rooms", nullable = false)
    private Integer occupiedRooms;
}
