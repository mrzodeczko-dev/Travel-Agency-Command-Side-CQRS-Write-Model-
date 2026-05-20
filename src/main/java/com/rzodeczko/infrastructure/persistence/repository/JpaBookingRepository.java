package com.rzodeczko.infrastructure.persistence.repository;

import com.rzodeczko.infrastructure.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface JpaBookingRepository extends JpaRepository<BookingEntity, Long> {
    @Query("select b from BookingEntity b where b.hotelId = :hotelId and b.startDate <= :reqEnd and b.endDate >= :reqStart")
    List<BookingEntity> findPotentialConflicts(Long hotelId, LocalDate reqStart, LocalDate reqEnd);
}

