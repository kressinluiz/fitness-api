package com.kressin.fitness_app.dto;

import java.util.List;

import com.kressin.fitness_app.entity.ScheduleType;

public record CreateWorkoutDateRequest(
        ScheduleType scheduleType,
        List<CreateScheduleEntryRequest> scheduleEntries) {
}
