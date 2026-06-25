package com.kressin.fitness_app.dto;

import java.time.ZonedDateTime;

public record ScheduleEntryResponse(
        Long id,
        Integer weekDay,
        ZonedDateTime dateTime) {
}
