package com.rzodeczko.infrastructure.kafka.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.topics.hotels")
public record HotelTopicProperties(
        String name
) {
}
