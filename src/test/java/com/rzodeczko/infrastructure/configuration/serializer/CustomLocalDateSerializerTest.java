package com.rzodeczko.infrastructure.configuration.serializer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CustomLocalDateSerializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new CustomLocalDateSerializer());
        objectMapper = JsonMapper.builder()
                .addModule(module)
                .build();
    }

    @Test
    void serialize_typicalDate_formatsAsIsoString() throws Exception {
        LocalDate date = LocalDate.of(2027, 8, 15);

        String json = objectMapper.writeValueAsString(date);

        assertThat(json).isEqualTo("\"2027-08-15\"");
    }

    @Test
    void serialize_singleDigitMonthAndDay_padsWithZero() throws Exception {
        LocalDate date = LocalDate.of(2027, 1, 5);

        String json = objectMapper.writeValueAsString(date);

        assertThat(json).isEqualTo("\"2027-01-05\"");
    }

    @Test
    void serialize_firstDayOfYear_formatsCorrectly() throws Exception {
        LocalDate date = LocalDate.of(2027, 1, 1);

        String json = objectMapper.writeValueAsString(date);

        assertThat(json).isEqualTo("\"2027-01-01\"");
    }
}
