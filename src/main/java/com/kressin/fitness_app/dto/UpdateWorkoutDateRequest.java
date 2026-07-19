package com.kressin.fitness_app.dto;

import java.util.List;

import com.kressin.fitness_app.entity.ScheduleType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateWorkoutDateRequest(
        @NotNull Long id,
        ScheduleType scheduleType,
        List<@Valid UpdateScheduleEntryRequest> scheduleEntries) {

}
