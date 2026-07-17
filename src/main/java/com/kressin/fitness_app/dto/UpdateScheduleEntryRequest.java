package com.kressin.fitness_app.dto;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;

public record UpdateScheduleEntryRequest(
        Long id,
        Boolean shouldDelete,
        DayOfWeek weekDay,
        ZonedDateTime dateTime) {

}
