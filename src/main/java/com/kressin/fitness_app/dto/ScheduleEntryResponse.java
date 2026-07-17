package com.kressin.fitness_app.dto;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;

public record ScheduleEntryResponse(
        Long id,
        DayOfWeek weekDay,
        ZonedDateTime dateTime) {
}
