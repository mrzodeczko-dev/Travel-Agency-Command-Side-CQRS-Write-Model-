package com.rzodeczko.infrastructure.configuration.serializer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CustomLocalDateDeserializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDate.class, new CustomLocalDateDeserializer());
        objectMapper = JsonMapper.builder()
                .addModule(module)
                .build();
    }

    @Test
    void deserialize_typicalIsoString_parsesCorrectly() throws Exception {
        LocalDate result = objectMapper.readValue("\"2027-08-15\"", LocalDate.class);

        assertThat(result).isEqualTo(LocalDate.of(2027, 8, 15));
    }

    @Test
    void deserialize_paddedMonthAndDay_parsesCorrectly() throws Exception {
        LocalDate result = objectMapper.readValue("\"2027-01-05\"", LocalDate.class);

        assertThat(result).isEqualTo(LocalDate.of(2027, 1, 5));
    }

    @Test
    void deserialize_firstDayOfYear_parsesCorrectly() throws Exception {
        LocalDate result = objectMapper.readValue("\"2027-01-01\"", LocalDate.class);

        assertThat(result).isEqualTo(LocalDate.of(2027, 1, 1));
    }
}
