package com.kressin.fitness_app.dto;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;

import jakarta.validation.constraints.FutureOrPresent;

public record UpdateScheduleEntryRequest(
        Long id,
        Boolean shouldDelete,
        DayOfWeek weekDay,
        @FutureOrPresent ZonedDateTime dateTime) {

}
