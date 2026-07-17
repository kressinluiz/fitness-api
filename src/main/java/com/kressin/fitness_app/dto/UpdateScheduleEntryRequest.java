package com.kressin.fitness_app.dto;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotNull;

public record UpdateScheduleEntryRequest(
        @NotNull Long id,
        Boolean shouldDelete,
        DayOfWeek weekDay,
        ZonedDateTime dateTime) {

}
