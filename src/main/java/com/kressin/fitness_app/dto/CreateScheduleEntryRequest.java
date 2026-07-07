package com.kressin.fitness_app.dto;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotNull;

public record CreateScheduleEntryRequest(
        @NotNull Integer weekDay,
        @NotNull ZonedDateTime dateTime) {
}
