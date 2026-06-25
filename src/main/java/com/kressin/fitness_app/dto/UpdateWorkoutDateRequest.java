package com.kressin.fitness_app.dto;

import java.util.List;

import com.kressin.fitness_app.entity.ScheduleType;

public record UpdateWorkoutDateRequest(
    Long id,
    ScheduleType scheduleType,
    List<UpdateScheduleEntryRequest> scheduleEntries
) {
    
}
