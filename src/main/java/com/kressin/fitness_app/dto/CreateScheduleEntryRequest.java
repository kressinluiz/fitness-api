package com.kressin.fitness_app.dto;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotNull;

public record CreateScheduleEntryRequest(
        @NotNull DayOfWeek weekDay,
        @NotNull ZonedDateTime dateTime) {
}
