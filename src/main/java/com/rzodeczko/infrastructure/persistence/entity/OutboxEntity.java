package com.rzodeczko.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OutboxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private String aggregateId; // partitioningKey

    private String type;

    @Lob
    private String payload;  // JSON
    private LocalDateTime createdAt;
}
