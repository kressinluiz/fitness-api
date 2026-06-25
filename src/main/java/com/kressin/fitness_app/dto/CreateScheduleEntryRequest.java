package com.kressin.fitness_app.dto;

import java.time.ZonedDateTime;

public record CreateScheduleEntryRequest(
        Integer weekDay,
        ZonedDateTime dateTime) {
}
