package com.kressin.fitness_app.dto;

import java.time.ZonedDateTime;

public record UpdateScheduleEntryRequest(
    Long id,
    Integer weekDay,
    ZonedDateTime dateTime
) {
    
}
