
package com.unilak.expensetracker.expense_tracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Application configuration class
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Configuration
public class AppConfig {

    /**
     * Object mapper for JSON serialization/deserialization
     * Configured to properly handle Java 8 date/time types like LocalDateTime
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Add JavaTimeModule to support Java 8 date/time types
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Format dates as ISO strings
        return objectMapper;
    }
}