package com.rzodeczko.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rzodeczko.application.port.in.CreateBookingUseCase;
import com.rzodeczko.application.port.out.TravelRepository;
import com.rzodeczko.application.service.BookingService;
import com.rzodeczko.infrastructure.tx.TransactionalCreateBookingUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public BookingService bookingService(TravelRepository travelRepository) {
        return new BookingService(travelRepository);
    }

    @Bean
    @Qualifier("transactionalCreateBookingUseCase")
    public CreateBookingUseCase transactionalCreateBookingUseCase(BookingService bookingService) {
        return new TransactionalCreateBookingUseCase(bookingService);
    }
}
